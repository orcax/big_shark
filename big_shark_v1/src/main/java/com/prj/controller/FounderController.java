package com.prj.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.DataWrapper;
import com.prj.entity.Education;
import com.prj.entity.Founder;
import com.prj.entity.Project.Phase;
import com.prj.entity.Work;

/**
 * The Class FounderController.
 */
@RestController
public class FounderController extends BaseController {

    /**
     * Checks if is founder valid.
     *
     * @param fid the fid
     * @return the data wrapper
     */
    private DataWrapper isFounderValid(Long fid) {
        Founder founder = founderService.get(fid);
        if (founder == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_NOT_FOUND,
                    String.format("The founder (ID=%d) does not exist.", fid));
        }
        return isProjectValid(founder.getProjectId(), Phase.ACCEPTED);
    }

    /**
     * Checks if is work valid.
     *
     * @param fid the fid
     * @param wid the wid
     * @return the data wrapper
     */
    private DataWrapper isWorkValid(Long fid, Long wid) {
        Work work = founderService.getWork(wid);
        if (work == null || work.getFounderId() != fid) {
            return new DataWrapper(
                    ErrorCodeEnum.BIZ_DATA_NOT_FOUND,
                    String.format(
                            "The founder (ID=%d) has no work experience (ID=%d).",
                            fid, wid));
        }
        return isFounderValid(fid);
    }

    /**
     * Checks if is edu valid.
     *
     * @param fid the fid
     * @param eid the eid
     * @return the data wrapper
     */
    private DataWrapper isEduValid(Long fid, Long eid) {
        Education edu = founderService.getEdu(eid);
        if (edu == null || edu.getFounderId() != fid) {
            return new DataWrapper(
                    ErrorCodeEnum.BIZ_DATA_NOT_FOUND,
                    String.format(
                            "The founder (ID=%d) has no education experience (ID=%d).",
                            fid, eid));
        }
        return isFounderValid(fid);
    }

    /**
     * 读取创始人信息。
     *
     * @param fid the fid
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}", method = RequestMethod.GET)
    DataWrapper read(@PathVariable long fid) {
        DataWrapper check = isFounderValid(fid);
        return check != null ? check : founderService.read(fid);
    }

    /**
     * 列出项目的创始人信息。
     *
     * @param projectId the project id
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder", method = RequestMethod.GET)
    DataWrapper listByProject(@RequestParam long projectId) {
        DataWrapper check = isProjectValid(projectId, Phase.ACCEPTED);
        return check != null ? check : founderService.listByProject(projectId);
    }

    /**
     * 添加创始人
     *
     * @param founder the founder
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder", method = RequestMethod.POST)
    DataWrapper add(@RequestBody Founder founder) {
        DataWrapper check = isProjectValid(founder.getProjectId(),
                Phase.ACCEPTED);
        return check != null ? check : founderService.create(founder);
    }

    /**
     * 更新创始人信息
     *
     * @param fid the fid
     * @param founder the founder
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}", method = RequestMethod.PUT)
    DataWrapper edit(@PathVariable long fid, @RequestBody Founder founder) {
        DataWrapper check = isFounderValid(fid);
        return check != null ? check : founderService.update(fid, founder);
    }

    /**
     * 删除创始人信息，同时删除关联的工作和教育信息。
     *
     * @param fid the fid
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}", method = RequestMethod.DELETE)
    DataWrapper delete(@PathVariable long fid) {
        DataWrapper check = isFounderValid(fid);
        return check != null ? check : founderService.delete(fid);
    }

    /**
     * 列出该创始人的工作信息。
     *
     * @param fid the fid
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}/work", method = RequestMethod.GET)
    DataWrapper listWorkByFounder(@PathVariable long fid) {
        DataWrapper check = isFounderValid(fid);
        return check != null ? check : founderService.listWorkByFounder(fid);
    }

    /**
     * 添加工作经历。
     *
     * @param fid the fid
     * @param work the work
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}/work", method = RequestMethod.POST)
    DataWrapper addWork(@PathVariable long fid, @RequestBody Work work) {
        DataWrapper check = isFounderValid(fid);
        work.setFounderId(fid);
        return check != null ? check : founderService.createWork(work);
    }

    /**
     * 更新工作经历。
     *
     * @param fid the fid
     * @param wid the wid
     * @param work the work
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}/work/{wid}", method = RequestMethod.PUT)
    DataWrapper editWork(@PathVariable long fid, @PathVariable long wid,
            @RequestBody Work work) {
        DataWrapper check = isWorkValid(fid, wid);
        return check != null ? check : founderService.updateWork(wid, work);
    }

    /**
     * 删除工作经历。
     *
     * @param fid the fid
     * @param wid the wid
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}/work/{wid}", method = RequestMethod.DELETE)
    DataWrapper deleteWork(@PathVariable long fid, @PathVariable long wid) {
        DataWrapper check = isWorkValid(fid, wid);
        return check != null ? check : founderService.deleteWork(wid);
    }

    /**
     * 列出该创始人的教育信息。
     *
     * @param fid the fid
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}/edu", method = RequestMethod.GET)
    DataWrapper listEducation(@PathVariable long fid) {
        DataWrapper check = isFounderValid(fid);
        return check != null ? check : founderService.listEduByFounder(fid);
    }

    /**
     * 添加教育信息。
     *
     * @param fid the fid
     * @param edu the edu
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}/edu", method = RequestMethod.POST)
    DataWrapper addEducation(@PathVariable long fid, @RequestBody Education edu) {
        DataWrapper check = isFounderValid(fid);
        edu.setFounderId(fid);
        return check != null ? check : founderService.createEdu(edu);
    }

    /**
     * 更新教育经历。
     *
     * @param fid the fid
     * @param eid the eid
     * @param edu the edu
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}/edu/{eid}", method = RequestMethod.PUT)
    DataWrapper editEducation(@PathVariable long fid, @PathVariable long eid,
            @RequestBody Education edu) {
        DataWrapper check = isEduValid(fid, eid);
        return check != null ? check : founderService.updateEdu(eid, edu);
    }

    /**
     * 删除教育经历。
     *
     * @param fid the fid
     * @param eid the eid
     * @return the data wrapper
     */
    @RequestMapping(value = "/founder/{fid}/edu/{eid}", method = RequestMethod.DELETE)
    DataWrapper deleteEducation(@PathVariable long fid, @PathVariable long eid) {
        DataWrapper check = isEduValid(fid, eid);
        return check != null ? check : founderService.deleteEdu(eid);
    }
}
