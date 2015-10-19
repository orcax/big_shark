package com.prj.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prj.common.enums.ErrorCodeEnum;
import com.prj.common.util.DataWrapper;
import com.prj.entity.Company;
import com.prj.entity.Project.Phase;

/**
 * The Class CompanyController.
 */
@RestController
public class CompanyController extends BaseController {

    /**
     * Checks if is company valid.
     *
     * @param cid the cid
     * @return the data wrapper
     */
    private DataWrapper isCompanyValid(Long cid) {
        Company company = companyService.get(cid);
        if (company == null) {
            return new DataWrapper(ErrorCodeEnum.BIZ_DATA_NOT_FOUND, String.format(
                "The company (ID=%d) does not exist.", cid));
        }
        return isProjectValid(company.getProjectId(), Phase.ACCEPTED);
    }

    /**
     * 读取项目对应的公司信息。phase = [ACCEPTED]
     *
     * @param projectId the project id
     * @return the data wrapper
     */
    @RequestMapping(value = "/company", method = RequestMethod.GET)
    DataWrapper readByProject(@RequestParam long projectId) {
        DataWrapper check = isProjectValid(projectId, Phase.ACCEPTED);
        return check != null ? check : companyService.readByProject(projectId);
    }

    /**
     * 添加项目对应的公司信息。每个项目对应唯一的公司信息，所以同一个项目不能添加多个公司记录。phase = [ACCEPTED]
     *
     * @param company the company
     * @return the data wrapper
     */
    @RequestMapping(value = "/company", method = RequestMethod.POST)
    DataWrapper add(@RequestBody Company company) {
        DataWrapper check = isProjectValid(company.getProjectId(), Phase.ACCEPTED);
        return check != null ? check : companyService.create(company);
    }

    /**
     * 更新公司信息。phase = [ACCEPTED]
     *
     * @param cid the cid
     * @param company the company
     * @return the data wrapper
     */
    @RequestMapping(value = "/company/{cid}", method = RequestMethod.PUT)
    DataWrapper edit(@PathVariable long cid, @RequestBody Company company) {
        DataWrapper check = isCompanyValid(cid);
        return check != null ? check : companyService.update(cid, company);
    }

}
