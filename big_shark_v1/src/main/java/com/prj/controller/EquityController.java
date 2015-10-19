package com.prj.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.exception.AppException;
import com.prj.common.util.DataWrapper;
import com.prj.common.util.FileObject;
import com.prj.entity.Equity;
import com.prj.entity.EquityFile;
import com.prj.entity.EquityFile.Type;
import com.prj.entity.Project.Phase;

/**
 * The Class EquityController.
 */
@RestController
public class EquityController extends BaseController{

    /**
     * Checks if is equity valid.
     *
     * @param eid the eid
     * @return the data wrapper
     */
    private DataWrapper isEquityValid(Long eid) {
        Equity equity = equityService.get(eid);
        if (equity == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_NOT_FOUND,
                    String.format("The equity (ID=%d) does not exist.", eid));
        }
        return isProjectValid(equity.getProjectId(), Phase.ACCEPTED);
    }

    /**
     * Checks if is file valid.
     *
     * @param eid the eid
     * @param fid the fid
     * @return the data wrapper
     */
    private DataWrapper isFileValid(Long eid, Long fid) {
        EquityFile ef = equityService.getFile(fid);
        if (ef == null || ef.getEquityId() != eid) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_NOT_FOUND,
                    String.format(
                            "The equity (ID=%d) has no such file (ID=%d).",
                            eid, fid));
        }
        return isEquityValid(eid);
    }

    /**
     * 列出正式项目的股权变更信息。
     *
     * @param projectId the project id
     * @return the data wrapper
     */
    @RequestMapping(value = "/equity", method = RequestMethod.GET)
    DataWrapper listByProject(@RequestParam long projectId) {
        DataWrapper check = isProjectValid(projectId, Phase.ACCEPTED);
        return check != null ? check : equityService.listByProject(projectId);
    }

    /**
     * 添加股权信息。
     *
     * @param equity the equity
     * @return the data wrapper
     */
    @RequestMapping(value = "/equity", method = RequestMethod.POST)
    DataWrapper add(@RequestBody Equity equity) {
        DataWrapper check = isProjectValid(equity.getProjectId(),
                Phase.ACCEPTED);
        return check != null ? check : equityService.create(equity);
    }

    /**
     * 编辑股权信息。
     *
     * @param eid the eid
     * @param equity the equity
     * @return the data wrapper
     */
    @RequestMapping(value = "/equity/{eid}", method = RequestMethod.PUT)
    DataWrapper edit(@PathVariable long eid, @RequestBody Equity equity) {
        DataWrapper check = isEquityValid(eid);
        return check != null ? check : equityService.update(eid, equity);
    }

    /**
     * 删除股权信息，同时删除关联的附件信息。
     *
     * @param eid the eid
     * @return the data wrapper
     */
    @RequestMapping(value = "/equity/{eid}", method = RequestMethod.DELETE)
    DataWrapper delete(@PathVariable long eid) {
        DataWrapper check = isEquityValid(eid);
        return check != null ? check : equityService.delete(eid);
    }

    /**
     * 列出股权的附件信息。
     *
     * @param eid the eid
     * @return the data wrapper
     */
    @RequestMapping(value = "/equity/{eid}/file", method = RequestMethod.GET)
    DataWrapper listFiles(@PathVariable long eid) {
        DataWrapper check = isEquityValid(eid);
        return check != null ? check : equityService.listFiles(eid);
    }

    /**
     * 上传股权的新附件，每种类型的附件仅保存一份，所以对于相同类型的附件来说，新附件会覆盖旧的附件。
     *
     * @param eid the eid
     * @param type the type
     * @param file the file
     * @return the data wrapper
     */
    @RequestMapping(value = "/equity/{eid}/file", method = RequestMethod.POST)
    DataWrapper addFile(@PathVariable long eid, @RequestParam Type type,
            @RequestParam MultipartFile file) {
        DataWrapper check = isEquityValid(eid);
        return check != null ? check : equityService.addFile(eid, type, file);
    }

    /**
     * 下载附件
     *
     * @param eid the eid
     * @param fid the fid
     * @param resp the resp
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/equity/{eid}/file/{fid}", method = RequestMethod.GET)
    void downloadFile(@PathVariable long eid, @PathVariable long fid,
            HttpServletResponse resp) throws IOException {
        DataWrapper check = isFileValid(eid, fid);
        if (check != null) {
            throw AppException.newInstanceOfNotFoundException(check
                    .getMessageDetail());
        }
        FileObject fo = equityService.readFile(fid);
        EquityFile ef = (EquityFile) fo.getInfo();
        String fileName = ef.getName() + "."
                + ef.getKind().toString().toLowerCase();
        fileName = new String(fileName.getBytes("utf-8"), "utf-8");
        resp.setContentType(FileObject.getContentType(ef.getKind()));
        resp.setHeader("Content-Disposition", "attachment;filename=\""
                + fileName + "\"");
        ServletOutputStream os = resp.getOutputStream();
        os.write(fo.getData());
        os.flush();
        os.close();
    }

    /**
     * 删除附件
     *
     * @param eid the eid
     * @param fid the fid
     * @return the data wrapper
     */
    @RequestMapping(value = "/equity/{eid}/file/{fid}", method = RequestMethod.DELETE)
    DataWrapper deleteFile(@PathVariable long eid, @PathVariable long fid) {
        DataWrapper check = isFileValid(eid, fid);
        return check != null ? check : equityService.deleteFile(fid);
    }
}
