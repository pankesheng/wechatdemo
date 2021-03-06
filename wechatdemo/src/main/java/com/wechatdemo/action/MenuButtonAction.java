package com.wechatdemo.action;

import java.io.File;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pks.wechat.material.ArticleMaterial;
import com.pks.wechat.material.MaterialNews;
import com.pks.wechat.material.NewsMaterial;
import com.pks.wechat.menu.Button;
import com.pks.wechat.menu.ClickButton;
import com.pks.wechat.menu.ComplexButton;
import com.pks.wechat.menu.MaterialButton;
import com.pks.wechat.menu.Menu;
import com.pks.wechat.menu.QrcodeButton;
import com.pks.wechat.menu.ViewButton;
import com.pks.wechat.message.resp.Article;
import com.pks.wechat.pojo.WeChatMedia;
import com.pks.wechat.util.Page;
import com.pks.wechat.util.SUtilBase;
import com.pks.wechat.util.SUtilMaterial;
import com.pks.wechat.util.SUtilSend;
import com.pks.wechat.util.WechatApiHelper;
import com.wechatdemo.common.Configuration;
import com.wechatdemo.common.ZwPageResult;
import com.wechatdemo.entity.MenuButton;
import com.wechatdemo.redis.RedisUtils;
import com.wechatdemo.redis.RedisUtils2;
import com.wechatdemo.redis.RedisUtils3;
import com.wechatdemo.service.MenuButtonService;
import com.zcj.util.UtilString;
import com.zcj.web.dto.ServiceResult;
import com.zcj.web.springmvc.action.BasicAction;

@Controller
@RequestMapping(value="menubutton")
public class MenuButtonAction extends BasicAction {
	
	@Autowired
	private MenuButtonService menuButtonService;
	@Autowired
	private RedisUtils3 redisUtils3;
	
	@RequestMapping("/tolist")
	public String tolist(HttpServletRequest request,Model model){
		Map<String, Object> qbuilder = new HashMap<String, Object>();
		qbuilder.put("btn_list", 1);
		List<MenuButton> list = menuButtonService.find("btn_order asc", qbuilder, null);
		model.addAttribute("list", list);
		return "/WEB-INF/ftl/admin/weixin/menu_list.ftl";
	}
	
	@RequestMapping("/list")
	public void list(HttpServletRequest request,String btn_name,Integer btn_state,Long pid,PrintWriter out){
		String serverips = SUtilBase.getServerIps(Configuration.wechat_appId);
		System.out.println(serverips);
		Map<String, Object> qbuilder = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(btn_name)){
			qbuilder.put("btn_name", "%"+btn_name.trim()+"%");
		}
		if(btn_state!=null){
			qbuilder.put("btn_state", btn_state);
		}
		if(pid!=null){
			qbuilder.put("pid", pid);
		}
		
		List<MenuButton> list = menuButtonService.findByPage("btn_order asc", qbuilder);
		for (MenuButton obj : list) {
			if(obj.getPid()!=null){
				MenuButton parent = menuButtonService.findById(obj.getPid());
				obj.setShow_parent(parent==null?new MenuButton():parent);
			}else{
				obj.setShow_parent(new MenuButton());
			}
		}
		int total = menuButtonService.getTotalRows(qbuilder);
		page.setRows(list);
		page.setTotal(total);
		out.write(ZwPageResult.converByServiceResult(ServiceResult.initSuccess(page)));
	}
	
	@RequestMapping("/toadd")
	public String toadd(HttpServletRequest request,Model model){
		Map<String, Object> qbuilder = new HashMap<String, Object>();
		qbuilder.put("btn_list", 1);
		qbuilder.put("state", 1);
		List<MenuButton> list = menuButtonService.find("btn_order asc", qbuilder, null);
		model.addAttribute("list", list);
		List<String> key_map_keys = new ArrayList<String>();
        for (Iterator<Map.Entry<String, String>> it = MenuButton.KEY_MAP.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            key_map_keys.add(entry.getKey());
        }
        List<String> type_map_keys = new ArrayList<String>();
        for (Iterator<Map.Entry<String, String>> it = MenuButton.TYPE_MAP.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            type_map_keys.add(entry.getKey());
        }
        model.addAttribute("keykeys", key_map_keys);
        model.addAttribute("typekeys", type_map_keys);
		model.addAttribute("keymap", MenuButton.KEY_MAP);
		model.addAttribute("typemap", MenuButton.TYPE_MAP);
		return "/WEB-INF/ftl/admin/weixin/menu_modify.ftl";
	}
	
	@RequestMapping("/tomodify")
	public String tomodify(HttpServletRequest request,Long id , Model model){
		if(id==null){
			return "404.jsp";
		}
		MenuButton obj = redisUtils3.get(id.toString());
		if(obj==null){
			obj = menuButtonService.findById(id);
		}
		model.addAttribute("obj", obj);
		Map<String, Object> qbuilder = new HashMap<String, Object>();
		qbuilder.put("btn_list", 1);
		qbuilder.put("state", 1);
		List<MenuButton> list = menuButtonService.find("btn_order asc", qbuilder, null);
		model.addAttribute("list", list);
		List<String> key_map_keys = new ArrayList<String>();
        for (Iterator<Map.Entry<String, String>> it = MenuButton.KEY_MAP.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            key_map_keys.add(entry.getKey());
        }
        List<String> type_map_keys = new ArrayList<String>();
        for (Iterator<Map.Entry<String, String>> it = MenuButton.TYPE_MAP.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            type_map_keys.add(entry.getKey());
        }
        model.addAttribute("keykeys", key_map_keys);
        model.addAttribute("typekeys", type_map_keys);
		model.addAttribute("keymap", MenuButton.KEY_MAP);
		model.addAttribute("typemap", MenuButton.TYPE_MAP);
		return "/WEB-INF/ftl/admin/weixin/menu_modify.ftl";
	}
	
	@RequestMapping("/modify")
	public void modify(HttpServletRequest request,MenuButton obj,PrintWriter out){
		if(StringUtils.isBlank(obj.getBtn_name())){
			out.write(ServiceResult.initErrorJson("名称不能为空！"));
			return ;
		}
		if(obj.getPid()!=null){
			obj.setBtn_list(0);
			MenuButton pbutton = menuButtonService.findById(obj.getPid());
			if(pbutton==null){
				out.write(ServiceResult.initErrorJson("该一级菜单已经不存在，请重新选择！"));
				return ;
			}else {
				if(pbutton.getPid()!=null){
					out.write(ServiceResult.initErrorJson("选择失败，您不能将此按钮归属到二级按钮中去！"));
					return ;
				}
			}
			if(StringUtils.isBlank(obj.getBtn_type())|| !MenuButton.TYPE_MAP.containsKey(obj.getBtn_type())){
				out.write(ServiceResult.initErrorJson("按钮类型错误！"));
				return ;
			}
			if(MenuButton.TYPE_CLICK.equals(obj.getBtn_type())){
				obj.setBtn_url(null);
				if(StringUtils.isBlank(obj.getBtn_key()) || !MenuButton.KEY_MAP.containsKey(obj.getBtn_key())){
					out.write(ServiceResult.initErrorJson("没有该事件触发类型！"));
					return ;
				}
			}else if(MenuButton.TYPE_VIEW.equals(obj.getBtn_type())){
				obj.setBtn_key(null);
				if(StringUtils.isBlank(obj.getBtn_url())){
					out.write(ServiceResult.initErrorJson("请填写url地址！"));
					return ;
				}
			}
		}else{
			if(obj.getBtn_list()==null||obj.getBtn_list()==0){
				if(StringUtils.isBlank(obj.getBtn_type())|| !MenuButton.TYPE_MAP.containsKey(obj.getBtn_type())){
					out.write(ServiceResult.initErrorJson("按钮类型错误！"));
					return ;
				}
				if(MenuButton.TYPE_CLICK.equals(obj.getBtn_type())){
					obj.setBtn_url(null);
					obj.setBtn_media_id(null);
					obj.setBtn_media_name(null);
					if(StringUtils.isBlank(obj.getBtn_key()) || !MenuButton.KEY_MAP.containsKey(obj.getBtn_key())){
						out.write(ServiceResult.initErrorJson("没有该事件触发类型！"));
						return ;
					}
				}else if(MenuButton.TYPE_VIEW.equals(obj.getBtn_type())){
					obj.setBtn_key(null);
					obj.setBtn_media_id(null);
					obj.setBtn_media_name(null);
					if(StringUtils.isBlank(obj.getBtn_url())){
						out.write(ServiceResult.initErrorJson("请填写url地址！"));
						return ;
					}
				}else if(MenuButton.TYPE_MATERIAL.equals(obj.getBtn_type())){
					obj.setBtn_key(null);
					obj.setBtn_url(null);
					if(StringUtils.isBlank(obj.getBtn_media_id()) || StringUtils.isBlank(obj.getBtn_media_name())){
						out.write(ServiceResult.initErrorJson("请选择素材！"));
						return ;
					}
				}else if(MenuButton.TYPE_QRCODE.equals(obj.getBtn_type())){
					
				}
			}else{
				obj.setBtn_type(null);
				obj.setBtn_key(null);
				obj.setBtn_url(null);
			}
		}
		if(obj.getId()==null){
			obj.setId(UtilString.getLongUUID());
			menuButtonService.insert(obj);
		}else{
			menuButtonService.update(obj);
		}
		redisUtils3.set(obj.getId().toString(),obj);
		out.write(ServiceResult.initSuccessJson(null));
	}
	
	@RequestMapping("/remove")
	public void remove(HttpServletRequest request,String ids,PrintWriter out){
		if(StringUtils.isBlank(ids)){
			out.write(ServiceResult.initErrorJson("请选择要删除的信息！"));
			return ;
		}
		List<Long> idList = UtilString.stringToLongList(ids);
		if(idList.size()==1){
			menuButtonService.delete(idList.get(0));
		}else{
			menuButtonService.deleteByIds(idList);
		}
		out.write(ServiceResult.initSuccessJson(null));
	}
	
	@RequestMapping("/createMenu")
	public void createMenu(HttpServletRequest request,PrintWriter out){
		Map<String, Object> qbuilder = new HashMap<String, Object>();
		qbuilder.put("btn_state", 1);
		qbuilder.put("pidisnull", " ");
		List<MenuButton> buttons = menuButtonService.find("btn_order asc", qbuilder, 3);
		Button[] r1 = new Button[buttons.size()];
		for (int i = 0; i < buttons.size(); i++) {
			MenuButton b1 = buttons.get(i);
			if(b1.getBtn_list()!=null && b1.getBtn_list()==1){
				List<MenuButton> children = menuButtonService.find("btn_order asc", menuButtonService.initQbuilder(new String[]{"btn_state","pid"}, new Object[]{1,b1.getId()}), 5);
				if(children.size()==0){
					out.write(ServiceResult.initErrorJson("列表项:"+b1.getBtn_name()+"中必须包含子项！"));
					return ;
				}
				Button[] r2 = new Button[children.size()];
				for (int j = 0; j < children.size(); j++) {
					MenuButton b2 = children.get(j);
					if(MenuButton.TYPE_CLICK.equals(b2.getBtn_type())){
						ClickButton cb = new ClickButton(b2.getBtn_name(), b2.getBtn_key());
						r2[j] = cb;
					}else if(MenuButton.TYPE_VIEW.equals(b2.getBtn_type())){
						ViewButton vb = new ViewButton(b2.getBtn_name(), b2.getBtn_url());
						r2[j] = vb;
					}else if(MenuButton.TYPE_MATERIAL.equals(b2.getBtn_type())){
						MaterialButton mb = new MaterialButton(b2.getBtn_name(), b2.getBtn_media_id());
						r2[j] = mb;
					}else if(MenuButton.TYPE_QRCODE.equals(b2.getBtn_type())){
						QrcodeButton qb = new QrcodeButton(b2.getBtn_name(), b2.getBtn_type(), b2.getBtn_key(), new Button[]{});
						r2[j] = qb; 
					}
				}
				ComplexButton cb = new ComplexButton(b1.getBtn_name(), r2);
				r1[i] = cb;
			}else{
				if(MenuButton.TYPE_CLICK.equals(b1.getBtn_type())){
					ClickButton cb = new ClickButton(b1.getBtn_name(), b1.getBtn_key());
					r1[i] = cb;
				}else if(MenuButton.TYPE_VIEW.equals(b1.getBtn_type())){
					ViewButton vb = new ViewButton(b1.getBtn_name(), b1.getBtn_url());
					r1[i] = vb;
				}else if(MenuButton.TYPE_MATERIAL.equals(b1.getBtn_type())){
					MaterialButton mb = new MaterialButton(b1.getBtn_name(), b1.getBtn_media_id());
					r1[i] = mb;
				}else if(MenuButton.TYPE_QRCODE.equals(b1.getBtn_type())){
					QrcodeButton qb = new QrcodeButton(b1.getBtn_name(), b1.getBtn_type(), b1.getBtn_key(), new Button[]{});
					r1[i] = qb; 
				}
			}
		}
		Menu menu = new Menu();
		menu.setButton(r1);
		String result = WechatApiHelper.createMenuMsg(Configuration.wechat_appId,menu);
		if(StringUtils.isBlank(result)){
			out.write(ServiceResult.initSuccessJson(null));
		}else{
			out.write(ServiceResult.initErrorJson(result));
		}
	}
	
	@RequestMapping("/tomateriallist")
	public String tomateriallist(HttpServletRequest request,String type,Model model){
		
		String url = "http://pks.easy.echosite.cn/wechatdemo/upload/11223344.png";
		File file = new File(request.getSession().getServletContext().getRealPath("")+"/upload/11223344.png");
		try {
//			WeChatMedia media = SUtilMaterial.uploadTempMaterail(Configuration.wechat_appId, "image", file);
//			String media_id = media.getMediaId();
			String media_id = "x3tByUSjFPI7S5AdVzIVsx1P7PlnAG3QqM4pmz4A5MOrbK7-uUnXw_bxmJs-FpKI";
			List<ArticleMaterial> list = new ArrayList<ArticleMaterial>();
			list.add(new ArticleMaterial(media_id, "1", "1", "http://www.baidu.com", "1", "", null, null, null));
			WeChatMedia m = SUtilSend.uploadnews(Configuration.wechat_appId, list);
//			System.out.println(m.getMediaId());
			media_id = m.getMediaId();
//			media_id = "VkfeBPT51YoW82M1hxBbpMWqHzM-kT4BNZGB5mx2K4JxB_SX-OSwcqcO6j0nTUmO"; //uploadnew 后的media_id
			SUtilSend.sendPreviewMessage(Configuration.wechat_appId, SUtilSend.makeArticlePreviewMessage("oXWySjiKx0vEVHmMs-Ul9u8O4_vA",null, media_id));
//			List<String> openIds = new ArrayList<String>();
//			openIds.add("oXWySjiKx0vEVHmMs-Ul9u8O4_vA");
//			openIds.add("oXWySjszg5jxhM8pbSKrz5yriUJk");
//			SUtilSend.sendUserMessage(Configuration.wechat_appId, SUtilSend.makeImageUserMeesage(openIds, media_id));
//			SUtilSend.sendUserMessage(Configuration.wechat_appId, SUtilSend.makeArticleUserMessage(openIds, media_id, 0));
//			SUtilSend.sendTagMessage(Configuration.wechat_appId, SUtilSend.makeImageTagMessage(true, null, media_id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		WeChatMedia media = SUtilMaterial.uploadTempMaterail(Configuration.wechat_appId, "thumb", file);
//		System.out.println(media.getMediaId());
		model.addAttribute("type", type);
//		SUtilMaterial.upload(Configuration.wechat_appId, file, "thumb");
		String returnUrl = "404.jsp";
		if("news".equals(type)){
			returnUrl = "/WEB-INF/ftl/admin/weixin/material_news_list.ftl";
		}
		return returnUrl;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/materiallist")
	public void getMaterialList(PrintWriter out,String type) throws ParseException{
		Page page = WechatApiHelper.getMaterialList(Configuration.wechat_appId,type,0,20);
		if(page.getRows()!=null){
			if(StringUtils.isNotBlank(type) && "news".equals(type)){
				List<NewsMaterial> list = (List<NewsMaterial>) page.getRows();
				for (NewsMaterial obj : list) {
					obj.setUpdateDate(new Date(Long.parseLong(obj.getUpdate_time())*1000));
					List<MaterialNews> news = obj.getContent().getNews_item();
					for (MaterialNews obj2 : news) {
						if("1".equals(obj2.getShow_cover_pic())){
							obj.setName(obj2.getTitle());
						}
					}
				}
				page.setRows(list);
			}
		}
		com.zcj.web.dto.Page page1 = new com.zcj.web.dto.Page();
		page1.setRows(page.getRows());
		page1.setTotal(page.getTotal());
		out.write(ZwPageResult.converByServiceResult(ServiceResult.initSuccess(page1)));
	}
}
