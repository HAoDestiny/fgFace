package net.sh.rgface.controller.admin;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import net.sh.rgface.entity.PersonnelEntity;
import net.sh.rgface.entity.excel.PersonnelExcel;
import net.sh.rgface.entity.excel.importEntity.ImportPersonnelExcel;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.exception.ControllerException;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.serive.admin.AdminPersonnelService;
import net.sh.rgface.util.ExcelUtil;
import net.sh.rgface.util.Tool;
import net.sh.rgface.vo.BaseVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DESTINY on 2018/6/15.
 */

@RestController
@RequestMapping(value = "/api/admin/excel")
public class AdminExcelController {

    @Value(value = "${face.recognition.excel.personnel}")
    private String EXCEL_FILE_NAME;

    @Value(value = "${face.recognition.url}")
    private String ROOT_URL;

    @Resource
    private AdminPersonnelService adminPersonnelService;

    @RequestMapping(value = "/getExcel", method = RequestMethod.POST)
    public ResultPo getExcel(@RequestBody BaseVo baseVo) throws ControllerException {

        if (baseVo.getExcelType() < 0) {
            throw new ControllerException("参数错误");
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("SUCCESS");


        if (baseVo.getExcelType() == 0) {
            resultPo.setData(ROOT_URL + "/api/admin/excel/getPersonnelExcel");
        }

        return resultPo;
    }

    @RequestMapping(value = "/getPersonnelExcel", method = RequestMethod.GET)
    public void getPersonnelExcel(HttpServletResponse httpServletResponse) throws ControllerException {


        List<PersonnelExcel> personnelExcelList = new ArrayList<>();
        List<PersonnelEntity> personnelEntityList = adminPersonnelService.getPersonnelList();

        if (personnelEntityList != null) {

            if (personnelEntityList.size() > 0) {

                for (PersonnelEntity personnelEntity : personnelEntityList) {

                    PersonnelExcel personnelExcel = new PersonnelExcel();

                    personnelExcel.setId(personnelEntity.getId());
                    personnelExcel.setTruename(personnelEntity.getTruename());
                    personnelExcel.setIcCard(personnelEntity.getIcCard());
                    personnelExcel.setSex(personnelEntity.getSex());
                    personnelExcel.setMobile(personnelEntity.getMobile());
                    personnelExcel.setPersonnelGrade(personnelEntity.getPersonnelGrade());
                    personnelExcel.setPersonnelClassCode(personnelEntity.getPersonnelClassCode());
                    personnelExcel.setPersonnelCode(personnelEntity.getPersonnelCode());
                    personnelExcel.setPersonnelType(personnelEntity.getPersonnelType());
                    personnelExcel.setPersonnelStatus(personnelEntity.getPersonnelStatus());
                    personnelExcel.setStudyType(personnelEntity.getStudyType());
                    personnelExcel.setPassTag(personnelEntity.getPassTag());
                    personnelExcel.setPassword(personnelEntity.getPassword());

                    personnelExcelList.add(personnelExcel);
                }
            }
        }

        try {

            Workbook workbook = ExcelUtil.exportExcel(
                    personnelExcelList,
                    "学生信息",
                    "Personnel",
                    PersonnelExcel.class
            );

            httpServletResponse.setContentType("application/octet-stream");
            httpServletResponse.setHeader(
                    "Content-disposition",
                    "attachment;filename=" + URLEncoder.encode(EXCEL_FILE_NAME, "UTF-8")
            );

            httpServletResponse.flushBuffer();

            workbook.write(httpServletResponse.getOutputStream());

        } catch (IOException e) {

            throw new ControllerException("生成文件失败");
        }

    }


    @RequestMapping(value = "/importPersonnelExcel")
    public ResultPo importPersonnelExcel(@RequestParam("file") MultipartFile file, @RequestParam("type") String type,
                                         HttpServletRequest httpServletRequest, HttpSession httpSession) throws ControllerException {

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");
        if (adminSession == null) {
            throw new ControllerException("尚未登录");
        }

        if (file.isEmpty()) {
            throw new ControllerException("请选择上传文件");
        }

        if (null == type | type.length() == 0) {
            throw new ControllerException("参数错误");
        }

        ExcelImportResult<ImportPersonnelExcel> personnelEntities = ExcelUtil.importExcel(file, 0, 1, ImportPersonnelExcel.class);

        if (null == personnelEntities) {
            throw new ControllerException("导入失败");
        }

        System.out.println("----------- importPersonnelExcel isVerfiyFail = " + personnelEntities.isVerfiyFail() + ", successListSize:" + personnelEntities.getList().size());

        List<Integer> errorItem = new ArrayList<>();

        String ip = Tool.getIpAddress(httpServletRequest);
        //校验成功
        for (ImportPersonnelExcel importPersonnelExcel : personnelEntities.getList()) {

            //icCard不能相同
            PersonnelEntity personnelEntityImport = adminPersonnelService.findPersonnelEntityByIcCard(importPersonnelExcel.getIcCard());

            if (null != personnelEntityImport) {
                errorItem.add(importPersonnelExcel.getId());
                continue;
            }

            PersonnelEntity personnelEntity = new PersonnelEntity();

            personnelEntity.setMobile(importPersonnelExcel.getMobile());
            personnelEntity.setIcCard(importPersonnelExcel.getIcCard());
            personnelEntity.setPassword(importPersonnelExcel.getPassword());
            personnelEntity.setTruename(importPersonnelExcel.getTruename());
            personnelEntity.setSex(importPersonnelExcel.getSex());
            personnelEntity.setStudyType(importPersonnelExcel.getStudyType());
            personnelEntity.setPersonnelClassCode(importPersonnelExcel.getPersonnelClassCode());
            personnelEntity.setPersonnelCode(importPersonnelExcel.getPersonnelCode());
            personnelEntity.setPersonnelGrade(importPersonnelExcel.getPersonnelGrade());
            personnelEntity.setPersonnelType(importPersonnelExcel.getPersonnelType());
            personnelEntity.setPersonnelStatus(importPersonnelExcel.getPersonnelStatus()); //状态
            personnelEntity.setAccountId(adminSession.getId()); //所属账户

            personnelEntity.setLastLoginIp(ip);
            personnelEntity.setLastLoginTime((int) (System.currentTimeMillis() / 1000));
            personnelEntity.setFileId(1);
            personnelEntity.setFaceId(1);
            personnelEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
            personnelEntity.setPassTag(0);
            personnelEntity.setDeleteTag(0);

            adminPersonnelService.savePersonnelEntity(personnelEntity);

            if (personnelEntity.getId() == 0) {
                errorItem.add(importPersonnelExcel.getId());
            }
        }

        //校验失败
        if (personnelEntities.isVerfiyFail()) {
            for (ImportPersonnelExcel importPersonnelExcel : personnelEntities.getFailList()) {
                errorItem.add(importPersonnelExcel.getId());
            }
        }

        ResultPo resultPo = new ResultPo();
        resultPo.setStatus("SUCCESS");
        resultPo.setMessage("导入成功");
        resultPo.setData(errorItem);

        return resultPo;
    }
}
