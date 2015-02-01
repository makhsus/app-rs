package com.rs.composer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rs.dao.CityDao;
import com.rs.dao.ContentConfigDao;
import com.rs.dao.EmployeeDao;
import com.rs.dao.OccupationDao;
import com.rs.dao.ProvinceDao;
import com.rs.dao.SubOccupationDao;
import com.rs.dao.UsersDao;
import com.rs.model.City;
import com.rs.model.ContentConfig;
import com.rs.model.Employee;
import com.rs.model.Occupation;
import com.rs.model.Province;
import com.rs.model.SubOccupation;
import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class EmployeeComposer extends BaseComposer {
	
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(EmployeeComposer.class);
	
	private String patternDate = "DD-MM-YYYY";
	private String genderStr;
	private String bloodTypeStr;
	private String religionStr;
	private String maritalStatusStr;
	private String educationStr;
	List<Province> provinceList;
	private Province provinceSelected;
	List<City> cityList;
	private City citySelected;
	private Users userLogin;
	List<Occupation> occupationList;
	private Occupation occupationSelected;
	List<SubOccupation> subOccupationList;
	private SubOccupation subOccupationSelected;
	
	@Wire
	private Window winEmployee;
	@Wire
	private Listbox lbxEmployee, lbxReligion, lbxBloodType, lbxCities, lbxProvinces, lbxMaritalStatus, lbxEducation, lbxOccupation, lbxSubOccupation;
	@Wire
	private Grid grdAdd;
	@Wire
	private Textbox tbxNik, tbxFullName, tbxIdentityNumber, tbxPlaceOfBirth, tbxMajor, tbxPhoneNumber, tbxEmail, tbxVillage, tbxAddress;
	@Wire
	private Datebox dtbExpiredIdentityNumber, dtbDateOfBirth;
	@Wire
	private Radiogroup rgGender;
	
	@Listen ("onCreate = #winEmployee")
	public void win(){
		isLooged();
		userLogin = (Users)sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		loadDataEmployee();
		loadDataReligion();
		loadDataBloodType();
		loadDataProvinces();
		loadDataEducation();
		loadDataMaritalStatus();
		loadOccupation();
		grdAdd.setVisible(false);
		lbxEmployee.setVisible(true);
	}
	
	@Listen ("onClick = #tbnList")
	public void tbnListClick(){
		isLooged();
		userLogin = (Users)sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		loadDataEmployee();
		loadDataReligion();
		loadDataBloodType();
		loadDataProvinces();
		loadDataEducation();
		loadDataMaritalStatus();
		loadOccupation();
		lbxEmployee.setVisible(true);
		grdAdd.setVisible(false);
		winEmployee.setTitle("List Data Karyawan");
	}
	
	@Listen ("onClick = #tbnAdd")
	public void tbnAddClick(){
		lbxEmployee.setVisible(false);
		grdAdd.setVisible(true);
		winEmployee.setTitle("Tambah Karyawan Baru");
	}
	
	@Listen ("onClick = #btnAddEmployee")
	public void btnAddEmployeeClick(){
		String nik = tbxNik.getText();
		String fullName = tbxFullName.getText();
		String identityNumber = tbxIdentityNumber.getText();
		Date expIdentityNumber = dtbExpiredIdentityNumber.getValue();
		String gender = genderStr;
		String bloodType = bloodTypeStr;
		String religion = religionStr;
		String placeOfBirth = tbxPlaceOfBirth.getText();
		String phoneNumber = tbxPhoneNumber.getText();
		String email = tbxEmail.getText();
		String village = tbxVillage.getText();
		String address = tbxAddress.getText();
		String major = tbxMajor.getText();
		Date dateOfBirth = dtbDateOfBirth.getValue();
		String maritalStatus = maritalStatusStr;
		String education = educationStr;
		String status = "Y";
		
		Calendar dateNow = Calendar.getInstance();
		
		Employee employee = new Employee();
		EmployeeDao employeeDao = new EmployeeDao();
		employee.setAddress(address);
		employee.setBirthdate(dateOfBirth);
		employee.setDateExpiredIdentityNumber(expIdentityNumber);
		employee.setEmail(email);
		employee.setFullName(fullName);
		employee.setGender(gender);
		employee.setIdentityNumber(identityNumber);
		employee.setNik(nik);
		employee.setPhoneNumber(phoneNumber);
		employee.setRegisterDate(dateNow.getTime());
		employee.setStatus(status);
		employee.setCreateBy(userLogin);
		employee.setCityid(citySelected);
		employee.setSubOccupationId(subOccupationSelected);
		employee.setProvinceId(provinceSelected);
		employee.setBloodType(bloodType);
		employee.setMaritalStatus(maritalStatus);
		employee.setReligion(religion);
		employee.setVillage(village);
		employee.setEducation(education);
		employee.setMajor(major);
		employee.setPlaceOfBirth(placeOfBirth);
		employee.setSubOccupationId(subOccupationSelected);
		
		Messagebox.show("Simpan Data Karyawan Baru ?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
		
		try {
			employeeDao.setSessionFactory(sessionFactory);
			employeeDao.saveOrUpdate(employee);
			System.out.println("save employee "+nik+" : success");
			logger.info("save employee "+nik+" : success");
			Messagebox.show("Save/Update Success", "Success", Messagebox.OK, Messagebox.INFORMATION);
			win();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error save employee : " + e.getMessage());
			logger.info("error save employee : " + e.getMessage());
			Messagebox.show("Save/Update Failed", "Error", Messagebox.OK, Messagebox.ERROR);
		}
		
	}

	@Listen ("onCheck = #rgGender")
	public void rgGenderClick(){
		genderStr = rgGender.getSelectedItem().getValue();
	}
	
	@Listen ("onSelect = #lbxBloodType")
	public void lbxBloodTypeSelect(){
		bloodTypeStr = lbxBloodType.getSelectedItem().getValue();
	}
	
	@Listen ("onSelect = #lbxReligion")
	public void lbxReligionSelect(){
		religionStr = lbxReligion.getSelectedItem().getValue();
	}
	
	@Listen ("onSelect = #lbxMaritalStatus")
	public void lbxMaritalStatusSelect(){
		maritalStatusStr = lbxMaritalStatus.getSelectedItem().getValue();
	}
	
	@Listen ("onSelect = #lbxCities")
	public void lbxCitiesSelect(){
		if(cityList!=null){
			int index = lbxCities.getSelectedIndex();
			citySelected = cityList.get(index);
			}
	}
	
	@Listen ("onSelect = #lbxProvinces")
	public void lbxProvincesSelect(){
		if(provinceList!=null){
			int index = lbxProvinces.getSelectedIndex();
			provinceSelected = provinceList.get(index);
			loadDataCities();
			}
	}
	
	@Listen ("onSelect = #lbxEducation")
	public void lbxEducationSelect(){
		educationStr = lbxEducation.getSelectedItem().getValue();
	}
	
	@Listen ("onSelect = #lbxOccupation")
	public void lbxOccupationSelect(){
		if(occupationList!=null){
			int index = lbxOccupation.getSelectedIndex();
			occupationSelected = occupationList.get(index);
			loadSubOccupation();
			}
	}
	
	@Listen ("onSelect = #lbxSubOccupation")
	public void lbxSubOccupationSelect(){
		if(subOccupationList!=null){
			int index = lbxSubOccupation.getSelectedIndex();
			subOccupationSelected = subOccupationList.get(index);
			}
	}
	
	
	private void loadDataEmployee() {
		
		EmployeeDao dao = new EmployeeDao();
		UsersDao userDao = new UsersDao();
		dao.setSessionFactory(sessionFactory);
		userDao.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("status", "Y");
		List<Employee> listEmployee = dao.loadBy(Order.desc("idEmployee"), cr1);
				
				lbxEmployee.getItems().clear();
				int number = 1;
				
				for(Employee obj: listEmployee){
					Listitem li = new Listitem();
					lbxEmployee.appendChild(li);

					String registerDate = CommonUtil.dateFormat(obj.getRegisterDate(), patternDate);
					
					Listcell lc = new Listcell(Integer.toString(number++));
					li.appendChild(lc);
					lc = new Listcell(registerDate);
					li.appendChild(lc);
					lc = new Listcell(obj.getNik());
					li.appendChild(lc);
					lc = new Listcell(obj.getFullName());
					li.appendChild(lc);
					lc = new Listcell(obj.getPhoneNumber());
					li.appendChild(lc);
					lc = new Listcell(obj.getGender());
					li.appendChild(lc);
					lc = new Listcell(obj.getIdentityNumber());
					li.appendChild(lc);
					lc = new Listcell(obj.getSubOccupationId()!=null ? obj.getSubOccupationId().getSubOccupationName():"");
					li.appendChild(lc);
					lc = new Listcell(obj.getSubOccupationId().getOccupationId().getOccupationName());
					li.appendChild(lc);
					
				}
		
	}
	
	private void loadDataReligion() {
		ContentConfigDao dao = new ContentConfigDao();
		List<ContentConfig> list = dao.getDataReligion();
		lbxReligion.getItems().clear();
		lbxReligion.setModel(new ListModelList<>(list));
		ListitemRenderer<ContentConfig> renderer = new ListitemRenderer<ContentConfig>() {
			@Override
			public void render(Listitem item, ContentConfig obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getValue1()));
			}
		};
		lbxReligion.setItemRenderer(renderer);
		
		if (list.size() > 0) {
			lbxReligion.setSelectedIndex(0);
		}
		
	}
	
	private void loadDataBloodType() {
		ContentConfigDao dao = new ContentConfigDao();
		List<ContentConfig> list = dao.getDataBloodType();
		lbxBloodType.getItems().clear();
		lbxBloodType.setModel(new ListModelList<>(list));
		ListitemRenderer<ContentConfig> renderer = new ListitemRenderer<ContentConfig>() {
			@Override
			public void render(Listitem item, ContentConfig obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getValue1()));
			}
		};
		lbxBloodType.setItemRenderer(renderer);
		
		if (list.size() > 0) {
			lbxBloodType.setSelectedIndex(0);
		}
		
	}
	
	private void loadDataCities() {
		CityDao cityDao = new CityDao();
		cityDao.setSessionFactory(sessionFactory);
		Criterion cr = Restrictions.eq("provinces.provinceId", provinceSelected.getProvinceId());
		List<City> list = cityDao.loadBy(Order.asc("cityId"),cr);
		cityList = list;
		lbxCities.getItems().clear();
		lbxCities.setModel(new ListModelList<>(list));
		ListitemRenderer<City> renderer = new ListitemRenderer<City>() {
			@Override
			public void render(Listitem item, City obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getCityName()));
			}
		};
		lbxCities.setItemRenderer(renderer);
		
		if (list.size() > 0) {
			lbxCities.setSelectedIndex(0);
		}

	}
	
	private void loadDataProvinces() {
		ProvinceDao provinceDao = new ProvinceDao();
		provinceDao.setSessionFactory(sessionFactory);
		List<Province> list = provinceDao.loadAll(Order.asc("provinceId"));
		provinceList = list;
		lbxProvinces.getItems().clear();
		lbxProvinces.setModel(new ListModelList<>(list));
		ListitemRenderer<Province> renderer = new ListitemRenderer<Province>() {
			@Override
			public void render(Listitem item, Province obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getProvinceName()));
			}
		};
		lbxProvinces.setItemRenderer(renderer);

	}
	
	private void loadDataEducation() {
		ContentConfigDao dao = new ContentConfigDao();
		List<ContentConfig> list = dao.getDataEducation();
		lbxEducation.getItems().clear();
		lbxEducation.setModel(new ListModelList<>(list));
		ListitemRenderer<ContentConfig> renderer = new ListitemRenderer<ContentConfig>() {
			@Override
			public void render(Listitem item, ContentConfig obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getValue1()));
			}
		};
		lbxEducation.setItemRenderer(renderer);
		
		if (list.size() > 0) {
			lbxEducation.setSelectedIndex(0);
		}

	}
	
	private void loadDataMaritalStatus() {
		ContentConfigDao dao = new ContentConfigDao();
		List<ContentConfig> list = dao.getDataMaritalStatus();
		lbxMaritalStatus.getItems().clear();
		lbxMaritalStatus.setModel(new ListModelList<>(list));
		ListitemRenderer<ContentConfig> renderer = new ListitemRenderer<ContentConfig>() {
			@Override
			public void render(Listitem item, ContentConfig obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getValue1()));
			}
		};
		lbxMaritalStatus.setItemRenderer(renderer);
		
		if (list.size() > 0) {
			lbxMaritalStatus.setSelectedIndex(0);
		}

	}
	
	private void loadOccupation() {
		OccupationDao dao = new OccupationDao();
		dao.setSessionFactory(sessionFactory);
		List<Occupation> list = dao.loadAll(Order.asc("occupationId"));
		occupationList = list;
		lbxOccupation.getItems().clear();
		lbxOccupation.setModel(new ListModelList<>(list));
		ListitemRenderer<Occupation> renderer = new ListitemRenderer<Occupation>() {
		@Override
		public void render(Listitem item, Occupation obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getOccupationName()));
			}
		};
		lbxOccupation.setItemRenderer(renderer);
	}
	
	private void loadSubOccupation() {
		SubOccupationDao dao = new SubOccupationDao();
		dao.setSessionFactory(sessionFactory);
		Criterion cr = Restrictions.eq("occupationId.occupationId", occupationSelected.getOccupationId());
		List<SubOccupation> list = dao.loadBy(Order.asc("subOccupationId"), cr);
		subOccupationList = list;
		lbxSubOccupation.getItems().clear();
		lbxSubOccupation.setModel(new ListModelList<>(list));
		ListitemRenderer<SubOccupation> renderer = new ListitemRenderer<SubOccupation>() {
		@Override
		public void render(Listitem item, SubOccupation obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getSubOccupationName()));
			}
		};
		lbxSubOccupation.setItemRenderer(renderer);
		
		if (list.size() > 0) {
			lbxSubOccupation.setSelectedIndex(0);
		}
	}

}
