package net.sh.rgface.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by DESTINY on 2018/6/15.
 */
public class ExcelUtil {

    /**
     * @param list
     * @param title
     * @param sheetName
     * @param pojoClass
     * @param isCreateHeader
     * @return
     */
    public static Workbook exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, boolean isCreateHeader) {

        ExportParams exportParams = new ExportParams(title, sheetName);

        exportParams.setCreateHeadRows(isCreateHeader);

        return defaultExport(list, pojoClass, exportParams);
    }

    /**
     * @param list
     * @param title
     * @param sheetName
     * @param pojoClass
     * @return
     */
    public static Workbook exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass) {

       return defaultExport(list, pojoClass, new ExportParams(title, sheetName));
    }

    /**
     * @param list
     * @return
     */
    public static Workbook exportExcel(List<Map<String, Object>> list) {
       return defaultExport(list);
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {

    }

//    public static <T> List<T> importEntity(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
//        if (StringUtils.isBlank(filePath)) {
//            return null;
//        }
//        ImportParams params = new ImportParams();
//        params.setTitleRows(titleRows);
//        params.setHeadRows(headerRows);
//        List<T> list = null;
//        try {
//            list = ExcelImportUtil.importEntity(new File(filePath), pojoClass, params);
//        } catch (NoSuchElementException e) {
//
//            throw new NormalException("模板不能为空");
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            throw new NormalException(e.getMessage());
//        }
//
//        return list;
//    }
//
    public static <T> ExcelImportResult<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {

        if (file == null) {
            return null;
        }

        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedVerfiy(true); //校验

        ExcelImportResult<T> excelImportResult = null;
        try {

            excelImportResult = ExcelImportUtil.importExcelMore(file.getInputStream(), pojoClass, params);
        } catch (Exception e) {

            System.out.println(" ---------- importEntity: " + e.getMessage());
            return null;
        }

        return excelImportResult;
    }


    private static Workbook defaultExport(List<?> list, Class<?> pojoClass, ExportParams exportParams) {

        return ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
    }

    private static Workbook defaultExport(List<Map<String, Object>> list) {

        return ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
    }
}
