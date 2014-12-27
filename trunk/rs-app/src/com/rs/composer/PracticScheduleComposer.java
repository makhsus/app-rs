package com.rs.composer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.rs.dao.EmployeeDao;
import com.rs.dao.OccupationDao;
import com.rs.dao.PolyclinicDao;
import com.rs.dao.PracticeScheduleDao;
import com.rs.model.Employee;
import com.rs.model.Occupation;
import com.rs.model.Polyclinic;
import com.rs.model.PracticSchedule;
import com.rs.util.CommonUtil;

public class PracticScheduleComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private List<Polyclinic> listPoly;
	private Polyclinic selectedPoly;
	private List<Employee> listDoctor;
	private Employee selectedDoctor;
	private PracticSchedule selectedSchedule;
	
	
	@Wire
	private Window winSaveUpdate;
	@Wire
	private Listbox lbxPoly, lbxDocter;
	@Wire
	private Checkbox cbxH1, cbxH2, cbxH3, cbxH4, cbxH5, cbxH6, cbxH7;
	@Wire
	private Timebox tmxStart, tmxEnd;
	@Wire
	private Vlayout vlayt;
	@Wire
	private Radiogroup rgSchedule;
	
	
	@Listen ("onCreate = #winSchedule")
	public void winSchedule(){
		isLooged();
		loadSchedules();
	}
	
	@Listen ("onClick = #tbnList")
	public void tbnListClick(){
		System.out.println("list");
	}
	
	@Listen ("onClick = #tbnAdd")
	public void tbnAddClick(){
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/doctor/save_update.zul", null, null);
        windowAdd.doModal();
	}
	
	
	
	@Listen ("onCreate = #winSaveUpdate")
	public void winSaveUpdate(){
		isLooged();
		loadPoly();
		loadDoctor();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 5);
		cal.set(Calendar.MINUTE, 0);
		tmxStart.setValue(cal.getTime());
		
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 0);
		tmxEnd.setValue(cal.getTime());
		
	}
	
	@Listen ("onSelect = #lbxPoly")
	public void lbxPolySelect(){
		if(listPoly!=null){
			int index = lbxPoly.getSelectedIndex();
			selectedPoly = listPoly.get(index);
		}
	}
	
	@Listen ("onSelect = #lbxDocter")
	public void lbxDocterSelect(){
		if(listDoctor!=null){
			int index = lbxDocter.getSelectedIndex();
			selectedDoctor = listDoctor.get(index);
		}
	}
	
	@Listen ("onClick = #btnSubmit")
	public void btnSubmitClick(){
		if(selectedPoly==null){
			Messagebox.show("Silahkan pilih poliklinik", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		if(selectedDoctor==null){
			Messagebox.show("Silahkan pilih dokter", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		if(!cbxH1.isChecked() && !cbxH2.isChecked() && !cbxH3.isChecked() && !cbxH4.isChecked() && !cbxH5.isChecked() && !cbxH6.isChecked() && !cbxH7.isChecked()){
			Messagebox.show("Silahkan pilih hari", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		if(tmxStart.getValue().after(tmxEnd.getValue())){
			Messagebox.show("Jam yang dipilih tidak valid", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		String h1="", h2="", h3="", h4="", h5="", h6="", h7="";
		if(cbxH1.isChecked())h1="1-";
		if(cbxH2.isChecked())h2="2-";
		if(cbxH3.isChecked())h3="3-";
		if(cbxH4.isChecked())h4="4-";
		if(cbxH5.isChecked())h5="5-";
		if(cbxH6.isChecked())h6="6-";
		if(cbxH7.isChecked())h7="7";
		
		String days = h1+h2+h3+h4+h5+h6+h7;
		Date dateStart = tmxStart.getValue();
		Date dateEnd = tmxEnd.getValue();
		
		
		PracticSchedule ps = new PracticSchedule();
		ps.setPolyclinic(selectedPoly);
		ps.setDoctor(selectedDoctor);
		ps.setDays(days);
		ps.setStartTime(dateStart);
		ps.setEndTime(dateEnd);
		
		PracticeScheduleDao dao = new PracticeScheduleDao();
		dao.setSessionFactory(sessionFactory);
		dao.saveOrUpdate(ps);
		
		winSaveUpdate.detach();
	}
	
	@Listen ("onClick = #btnClose")
	public void btnCloseClick(){
		winSaveUpdate.detach();
	}
	
	@Listen ("onCheck = #rgSchedule")
	public void rgScheduleClick(){
		selectedSchedule = rgSchedule.getSelectedItem().getValue();
	}
	

	
	private void loadPoly(){
		selectedPoly = null;
		lbxPoly.getItems().clear();
		
		PolyclinicDao dao = new PolyclinicDao();
		dao.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("isActive", true);
		List<Polyclinic> list = dao.loadBy(Order.asc("polyclinicId"), cr1);
		listPoly = list;
		//System.out.println("listPoly: "+list.size());
		
		lbxPoly.setModel(new ListModelList<>(list));
		ListitemRenderer<Polyclinic> renderer = new ListitemRenderer<Polyclinic>() {
			@Override
			public void render(Listitem item, Polyclinic obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getPolyclinicName()));
			}
		};
		lbxPoly.setItemRenderer(renderer);
	}
	
	private void loadDoctor(){
		selectedDoctor = null;
		lbxDocter.getItems().clear();
		
		OccupationDao ocpDao = new OccupationDao();
		ocpDao.setSessionFactory(sessionFactory);
		Criterion crOcp1 = Restrictions.eq("occupationCode", "DR");
		List<Occupation> ocpList = ocpDao.loadBy(Order.asc("occupationId"), crOcp1);
		
		if(ocpList.size()>0){
			Occupation ocp = ocpList.get(0);
			
			EmployeeDao dao = new EmployeeDao();
			dao.setSessionFactory(sessionFactory);
			Criterion cr1 = Restrictions.eq("occupationId", ocp);
			List<Employee> list = dao.loadBy(Order.desc("idEmployee"), cr1);
			listDoctor = list;
			
			lbxDocter.setModel(new ListModelList<>(list));
			ListitemRenderer<Employee> renderer = new ListitemRenderer<Employee>() {
				@Override
				public void render(Listitem item, Employee obj, int index) throws Exception {
					String specialist = obj.getSpecialist()!=null?" - "+obj.getSpecialist():"";
					item.appendChild(new Listcell(obj.getFullName()+specialist));
				}
			};
			lbxDocter.setItemRenderer(renderer);
		}

	}
	
	@SuppressWarnings("unchecked")
	private void loadSchedules(){
		//vlayt.
		
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(PracticSchedule.class);
		cr.setProjection(Projections.distinct(Projections.property("polyclinic")));
		List<Polyclinic> scheduleList = cr.list();
		session.close();
		
		/*String rgId = "rgSchedule";
		Radiogroup rg = new Radiogroup();
		rg.setId(rgId);
		winSchedule.appendChild(rg);*/
		
		for(Polyclinic poly: scheduleList){
			Grid grid = new Grid();
			vlayt.appendChild(grid);
			
			Auxhead auxhead = new Auxhead();
			grid.appendChild(auxhead);
			
			Auxheader auxheader = new Auxheader();
			auxheader.setColspan(4);
			auxheader.setLabel(poly.getPolyclinicName());
			auxhead.appendChild(auxheader);
			
			Columns columns = new Columns();
			columns.appendChild(new Column("Nama Dokter", null, ""));
			columns.appendChild(new Column("Hari", null, "30%"));
			columns.appendChild(new Column("Jam", null, "20%"));
			columns.appendChild(new Column("#", null, "5%"));
			grid.appendChild(columns);
			
			Rows rows = new Rows();
			grid.appendChild(rows);
			
			
			session = sessionFactory.openSession();
			cr = session.createCriteria(PracticSchedule.class);
			cr.add(Restrictions.eq("polyclinic", poly));
			cr.setProjection(Projections.distinct(Projections.property("doctor")));
			List<Employee> epList = cr.list();
			session.close();
			
			for(Employee ep: epList){
				PracticeScheduleDao dao = new PracticeScheduleDao();
				dao.setSessionFactory(sessionFactory);
				Criterion crPs1 = Restrictions.eq("polyclinic", poly);
				Criterion crPs2 = Restrictions.eq("doctor", ep);
				List<PracticSchedule> psList = dao.loadBy(Order.asc("startTime"), crPs1, crPs2);
				int psSize = psList.size();
				
				for(int i=0;i<psSize;i++){
					PracticSchedule ps = psList.get(i);
					String name = ep.getFullName();
					String hari = CommonUtil.daysConvertNumberToName(ps.getDays());
					String patternJam = "HH:mm";
					String jam = CommonUtil.dateFormat(ps.getStartTime(), patternJam)+"-"+
								 CommonUtil.dateFormat(ps.getEndTime(), patternJam);
					
					Radio radio = new Radio();
					radio.setId("radio"+ps.getId());
					radio.setRadiogroup(rgSchedule);
					radio.setValue(ps);
					
					if(psSize>1){
						if(i==0){
							/*Cell cell = new Cell();
							cell.setRowspan(psSize);
							cell.appendChild(new Label(name));*/
							
							Row row = new Row();
							//row.appendChild(cell);
							row.appendChild(new Label(name));
							row.appendChild(new Label(hari));
							row.appendChild(new Label(jam));
							row.appendChild(radio);
							rows.appendChild(row);
						}else{
							Row row = new Row();
							row.appendChild(new Label());
							row.appendChild(new Label(hari));
							row.appendChild(new Label(jam));
							row.appendChild(radio);
							rows.appendChild(row);
						}
					}else{
						Row row = new Row();
						row.appendChild(new Label(name));
						row.appendChild(new Label(hari));
						row.appendChild(new Label(jam));
						row.appendChild(radio);
						rows.appendChild(row);
					}
				}
			}
		}
	}
	
}
