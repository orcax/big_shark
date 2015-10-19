package com.prj.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.DataWrapper;
import com.prj.entity.Progress;
import com.prj.entity.Project.Phase;

/**
 * The Class ProgressController.
 */
@RestController
public class ProgressController extends BaseController {

    /**
     * Checks if is progress valid.
     *
     * @param pid the pid
     * @return the data wrapper
     */
    private DataWrapper isProgressValid(Long pid) {
        Progress progress = progressService.get(pid);
        if (progress == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_NOT_FOUND,
                    String.format("The progress (ID=%d) does not exist.", pid));
        }
        return isProjectValid(progress.getProjectId(), Phase.ACCEPTED);
    }

    /**
     * 列出正式项目的进展。
     *
     * @param projectId the project id
     * @return the data wrapper
     */
    @RequestMapping(value = "/progress", method = RequestMethod.GET)
    DataWrapper listByProject(@RequestParam long projectId) {
        DataWrapper check = isProjectValid(projectId, Phase.ACCEPTED);
        return check != null ? check : progressService.listByProject(projectId);
    }

    /**
     * 添加项目进展。
     *
     * @param progress the progress
     * @return the data wrapper
     */
    @RequestMapping(value = "/progress", method = RequestMethod.POST)
    DataWrapper add(@RequestBody Progress progress) {
        DataWrapper check = isProjectValid(progress.getProjectId(),
                Phase.ACCEPTED);
        return check != null ? check : progressService.create(progress);
    }

    /**
     * 更新进展。
     *
     * @param pid the pid
     * @param progress the progress
     * @return the data wrapper
     */
    @RequestMapping(value = "/progress/{pid}", method = RequestMethod.PUT)
    DataWrapper edit(@PathVariable long pid, @RequestBody Progress progress) {
        DataWrapper check = isProgressValid(pid);
        return check != null ? check : progressService.update(pid, progress);
    }

    /**
     * 删除进展。
     *
     * @param pid the pid
     * @return the data wrapper
     */
    @RequestMapping(value = "/progress/{pid}", method = RequestMethod.DELETE)
    DataWrapper delete(@PathVariable long pid) {
        DataWrapper check = isProgressValid(pid);
        return check != null ? check : progressService.delete(pid);
    }
}
