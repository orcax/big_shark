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

import com.prj.common.util.DataWrapper;
import com.prj.common.util.FileObject;
import com.prj.common.util.RequiredRole;
import com.prj.entity.Account.Role;
import com.prj.entity.Project;
import com.prj.entity.Project.InvestType;
import com.prj.entity.Project.Phase;
import com.prj.entity.Project.Status;
import com.prj.entity.ProjectFile;

/**
 * The Class ProjectController.
 */
@RestController
public class ProjectController extends BaseController {

    /**
     *  读取项目的基本信息。
     *
     * @param pid 项目ID
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/{pid}", method = RequestMethod.GET)
    DataWrapper view(@PathVariable long pid) {
        return projectService.read(pid);
    }

    /**
     * 获取当前用户的所有项目的信息。role = [CUSTOMER]
     *
     * @return the data wrapper
     */
    @RequestMapping(value = "/project", method = RequestMethod.GET)
    @RequiredRole(Role.CUSTOMER)
    DataWrapper listByCreater() {
        return projectService.listByCreater(currentAccount().getId());
    }

    /**
     * 根据条件搜索项目。role = [MANAGER]
     *
     * @param phase the phase
     * @param status the status
     * @param city the city
     * @param name the name
     * @param notified the notified
     * @param investType the invest type
     * @param pageSize the page size
     * @param pageNumber the page number
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/search", method = RequestMethod.GET)
    @RequiredRole(Role.MANAGER)
    DataWrapper search(@RequestParam(required = false) Phase phase,
                       @RequestParam(required = false) Status status,
                       @RequestParam(required = false) String city,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Boolean notified,
                       @RequestParam(required = false) InvestType investType,
                       @RequestParam(required = false) Integer pageSize,
                       @RequestParam(required = false) Integer pageNumber) {
        return projectService.search(phase, status, city, name, notified, investType, pageSize,
            pageNumber);
    }

    /**
     * 创建一个新的项目。role = [CUSTOMER]
     *
     * @param project the project
     * @return the data wrapper
     */
    @RequestMapping(value = "/project", method = RequestMethod.POST)
    @RequiredRole(Role.CUSTOMER)
    DataWrapper add(@RequestBody Project project) {
        project.setCreaterId(currentAccount().getId());
        return projectService.create(project);
    }

    /**
     * 更新项目的基本信息。phase = [DRAFT], role = [CUSTOMER]
     *
     * @param pid the pid
     * @param project the project
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/{pid}", method = RequestMethod.PUT)
    @RequiredRole(Role.CUSTOMER)
    DataWrapper edit(@PathVariable long pid, @RequestBody Project project) {
        DataWrapper check = isProjectValid(pid, Phase.DRAFT);
        return check != null ? check : projectService.update(pid, project);
    }

    /**
     * 提交项目进入海选阶段。phase = [DRAFT], role = [CUSTOMER]
     *
     * @param pid the pid
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/{pid}/submit", method = RequestMethod.POST)
    @RequiredRole(Role.CUSTOMER)
    DataWrapper submit(@PathVariable long pid) {
        DataWrapper check = isProjectValid(pid, Phase.DRAFT);
        return check != null ? check : projectService.submit(pid);
    }

    /**
     * 删除项目。phase = [DRAFT], role = [CUSTOMER]
     *
     * @param pid the pid
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/{pid}", method = RequestMethod.DELETE)
    @RequiredRole(Role.CUSTOMER)
    DataWrapper delete(@PathVariable long pid) {
        DataWrapper check = isProjectValid(pid, Phase.DRAFT);
        return check != null ? check : projectService.delete(pid);
    }

    /**
     * 获得项目的所有附件信息。
     *
     * @param pid the pid
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/{pid}/file", method = RequestMethod.GET)
    DataWrapper listFiles(@PathVariable long pid) {
        DataWrapper check = isProjectValid(pid, null, null);
        return check != null ? check : projectService.listFiles(pid);
    }

    /**
     * 上传新附件。phase = [DRAFT]
     *
     * @param pid the pid
     * @param file the file
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/{pid}/file", method = RequestMethod.POST)
    @RequiredRole(Role.CUSTOMER)
    DataWrapper addFile(@PathVariable long pid, @RequestParam MultipartFile file) {
        DataWrapper check = isProjectValid(pid, Phase.DRAFT);
        return check != null ? check : projectService.addFile(pid, file);
    }

    /**
     * 下载附件。
     *
     * @param pid the pid
     * @param fid the fid
     * @param resp the resp
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/project/{pid}/file/{fid}", method = RequestMethod.GET)
    void downloadFile(@PathVariable long pid, @PathVariable long fid, HttpServletResponse resp)
                                                                                               throws IOException {
        DataWrapper check = isProjectValid(pid, null, null);
        if (check != null) {
            return;
        }
        // TODO content-disposition is ???
        FileObject fo = projectService.readFile(fid);
        ProjectFile attach = (ProjectFile) fo.getInfo();
        String fileName = attach.getName() + "." + attach.getKind().toString().toLowerCase();
        fileName = new String(fileName.getBytes("utf-8"), "utf-8");
        resp.setContentType(FileObject.getContentType(attach.getKind()));
        resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        ServletOutputStream os = resp.getOutputStream();
        os.write(fo.getData());
        os.flush();
        os.close();
    }

    /**
     * 删除附件。phase = [DRAFT]。role = [CUSTOMER]
     *
     * @param pid the pid
     * @param fid the fid
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/{pid}/file/{fid}", method = RequestMethod.DELETE)
    @RequiredRole(Role.CUSTOMER)
    DataWrapper deleteFile(@PathVariable long pid, @PathVariable long fid) {
        DataWrapper check = isProjectValid(pid, Phase.DRAFT);
        return check != null ? check : projectService.deleteFile(fid);
    }

    /**
     * 更改项目所在的阶段。role = [MANAGER]
     *
     * @param pid the pid
     * @param phase the phase
     * @param investType the invest type
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/{pid}/phase/{phase}", method = RequestMethod.POST)
    @RequiredRole(Role.MANAGER)
    DataWrapper changePhase(@PathVariable long pid, @PathVariable Phase phase,
                            @RequestParam(required = false) InvestType investType) {
        DataWrapper check = isProjectValid(pid, null);
        return check != null ? check : projectService.changePhase(pid, phase, investType);
    }

    /**
     * 批量将项目设置为“已通知”。role = [MANAGER]
     *
     * @param projectIds the project ids
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/notify", method = RequestMethod.POST)
    @RequiredRole(Role.MANAGER)
    DataWrapper batchNotify(@RequestParam long[] projectIds) {
        return projectService.batchNotify(projectIds);
    }

    /**
     * 更改项目当前状态。role = [MANAGER], phase = [AUDITION, FIRST_REVIEW, FINAL_REVIEW]
     *
     * @param pid the pid
     * @param status the status
     * @return the data wrapper
     */
    @RequestMapping(value = "/project/{pid}/status/{status}", method = RequestMethod.POST)
    @RequiredRole(Role.MANAGER)
    DataWrapper changeStatus(@PathVariable long pid, @PathVariable Status status) {
        DataWrapper check = isProjectValid(pid, null);
        return check != null ? check : projectService.changeStatus(pid, status);
    }
}
