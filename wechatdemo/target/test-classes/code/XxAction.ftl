package ${packages}.action.${modules};

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ${packages}.common.ZwPageResult;
import ${packages}.entity.${modules}.${classes};
import ${packages}.service.${modules}.${classes}Service;
import com.zcj.util.UtilConvert;
import com.zcj.util.UtilString;
import com.zcj.web.dto.ServiceResult;
import com.zcj.web.springmvc.action.BasicAction;

@Controller
@RequestMapping("/${classes?uncap_first}")
@Scope("prototype")
@Component("${classes?uncap_first}Action")
public class ${classes}Action extends BasicAction {

	@Resource
	private ${classes}Service ${classes?uncap_first}Service;

	@RequestMapping("/tolist")
	public String tolist(Model model) {

		return "/WEB-INF/ftl/admin/${modules?lower_case}/${classes?lower_case}_list.ftl";
	}

	@RequestMapping("/list")
	public void list(<#list qbuilderList as q><#if q.listQuery><#if q.oper == "=" || q.oper == "like">${q.fieldType} search${q.fieldName?cap_first}, <#elseif q.oper == "time" || q.oper == "between">${q.fieldType} search${q.fieldName?cap_first}Begin, ${q.fieldType} search${q.fieldName?cap_first}End, </#if></#if></#list>PrintWriter out) {
		Map<String, Object> qbuilder = new HashMap<String, Object>();
		<#list qbuilderList as q>
			<#if q.listQuery>
				<#if q.oper == "=">
					<#if q.fieldType == "String">
		if (UtilString.isNotBlank(search${q.fieldName?cap_first})) {
			qbuilder.put("${q.fieldName}", search${q.fieldName?cap_first});
		}
					<#else>
		if (search${q.fieldName?cap_first} != null) {
			qbuilder.put("${q.fieldName}", search${q.fieldName?cap_first});
		}
					</#if>
				<#elseif q.oper == "like">
		if (UtilString.isNotBlank(search${q.fieldName?cap_first})) {
			qbuilder.put("${q.fieldName}", "%" + search${q.fieldName?cap_first} + "%");
		}
				<#elseif q.oper == "time" || q.oper == "between">
		if (search${q.fieldName?cap_first}Begin != null) {
			qbuilder.put("${q.fieldName}Begin", search${q.fieldName?cap_first}Begin);
		}
		if (search${q.fieldName?cap_first}End != null) {
			qbuilder.put("${q.fieldName}End", search${q.fieldName?cap_first}End);
		}
				</#if>
			</#if>
		</#list>
		page.setRows(${classes?uncap_first}Service.findByPage(null, qbuilder));
		page.setTotal(${classes?uncap_first}Service.getTotalRows(qbuilder));
		out.write(ZwPageResult.converByServiceResult(ServiceResult.initSuccess(page)));
	}

	@RequestMapping("/toadd")
	public String toadd(Model model) {
		return "/WEB-INF/ftl/admin/${modules?lower_case}/${classes?lower_case}_modify.ftl";
	}

	@RequestMapping("/tomodify/{id}")
	public String tomodify(@PathVariable Long id, Model model) {
		model.addAttribute("obj", ${classes?uncap_first}Service.findById(id));
		return "/WEB-INF/ftl/admin/${modules?lower_case}/${classes?lower_case}_modify.ftl";
	}

	@RequestMapping("/delete")
	public void delete(HttpServletRequest request, String ids, PrintWriter out) {
		if (UtilString.isBlank(ids)) {
			out.write(ServiceResult.initErrorJson("请选择需要删除的记录！"));
			return;
		}
		Long[] s = UtilConvert.string2Long(ids.split(","));
		${classes?uncap_first}Service.deleteByIds(Arrays.asList(s));
		out.write(ServiceResult.initSuccessJson(null));
	}
	
	@RequestMapping("/modify")
	public void modify(HttpServletRequest request, Long id, ${classes} obj, PrintWriter out) {
		ServiceResult sr = ServiceResult.initSuccess(null);
		if (obj == null) {
			sr = ServiceResult.initErrorParam();
		}

		if (sr.success()) {
			if (id == null) {
				obj.setId(UtilString.getLongUUID());
				${classes?uncap_first}Service.insert(obj);
			} else {
				${classes?uncap_first}Service.update(obj);
			}
			sr = ServiceResult.initSuccess(null);
		}
		out.write(ServiceResult.GSON_DT.toJson(sr));
	}

}
