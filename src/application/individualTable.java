package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class individualTable {	
	private StringProperty check;
	private StringProperty fName;
	private StringProperty mName;
	private StringProperty lName;
	private StringProperty sex;
	private StringProperty cheo;
	private StringProperty mshahara;
	private StringProperty tsd;
	private StringProperty kuzaliwa;
	private StringProperty ajira;
	private StringProperty thibitishwa;
	private StringProperty daraja;
	private StringProperty elimu;
	private StringProperty chuo;
	private StringProperty aliomaliza;
	private StringProperty sasaKazi;
	private StringProperty awaliKazi;
	private StringProperty dini;
	private StringProperty alipozaliwa;
	private StringProperty number;
	private StringProperty somo1;
	private StringProperty somo2;
	private StringProperty kata;
	
	
	public individualTable(String check,String fName,String mName,String lName,String sex,String cheo,String mshahara,String tsd,String kuzaliwa,String ajira,
			String thibitishwa,String daraja,String elimu,String chuo,String aliomaliza,String sasaKazi,String awaliKazi,String dini,String alipozaliwa,
			String number,String somo1,String somo2,String kata) {
		
		this.check = new SimpleStringProperty(check);
		this.fName = new SimpleStringProperty(fName);
		this.mName = new SimpleStringProperty(mName);
		this.lName = new SimpleStringProperty(lName);
		this.sex = new SimpleStringProperty(sex);
		this.cheo = new SimpleStringProperty(cheo);
		this.mshahara = new SimpleStringProperty(mshahara);
		this.tsd = new SimpleStringProperty(tsd);
		this.kuzaliwa = new SimpleStringProperty(kuzaliwa);
		this.ajira = new SimpleStringProperty(ajira);
		this.thibitishwa = new SimpleStringProperty(thibitishwa);
		this.daraja = new SimpleStringProperty(daraja);
		this.elimu = new SimpleStringProperty(elimu);
		this.chuo = new SimpleStringProperty(chuo);
		this.aliomaliza = new SimpleStringProperty(aliomaliza);
		this.sasaKazi = new SimpleStringProperty(sasaKazi);
		this.awaliKazi = new SimpleStringProperty(awaliKazi);
		this.dini = new SimpleStringProperty(dini);
		this.alipozaliwa = new SimpleStringProperty(alipozaliwa);
		this.number = new SimpleStringProperty(number);
		this.somo1 = new SimpleStringProperty(somo1);
		this.somo2 = new SimpleStringProperty(somo2);
		this.kata = new SimpleStringProperty(kata);
		
		
	}
	
	public void setcheck(String value) {
		check.set(value);
	}

	public void setfName(String value) {
		fName.set(value);
	}


	public void setmName(String value) {
		mName.set(value);
	}


	public void setlName(String value) {
		lName.set(value);
	}


	public void setSex(String value) {
		sex.set(value);
	}


	public void setCheo(String value) {
		cheo.set(value);
	}


	public void setMshahara(String value) {
		mshahara.set(value);
	}


	public void setTsd(String value) {
		tsd.set(value);
	}


	public void setKuzaliwa(String value) {
		kuzaliwa.set(value);
	}


	public void setAjira(String value) {
		ajira.set(value);
	}


	public void setThibitishwa(String value) {
		thibitishwa.set(value);
	}


	public void setDaraja(String value) {
		daraja.set(value);
	}


	public void setElimu(String value) {
		elimu.set(value);
	}


	public void setChuo(String value) {
		chuo.set(value);
	}


	public void setAliomaliza(String value) {
		aliomaliza.set(value);
	}


	public void setSasaKazi(String value) {
		sasaKazi.set(value);
	}


	public void setAwaliKazi(String value) {
		awaliKazi.set(value);
	}


	public void setDini(String value) {
		dini.set(value);
	}


	public void setAlipozaliwa(String value) {
		alipozaliwa.set(value);
	}


	public void setNumber(String value) {
		number.set(value);
	}


	public void setSomo1(String value) {
		somo1.set(value);
	}


	public void setSomo2(String value) {
		somo2.set(value);
	}


	public void setKata(String value) {
		kata.set(value);
	}
    
	
	
	
	
	
	
	
	public String check() {
		return check.get();
	}
	public String fName() {
		return fName.get();
	}
	public String mName() {
		return mName.get();
	}
	public String lName() {
		return lName.get();
	}
	public String sex() {
		return sex.get();
	}
	public String cheo() {
		return cheo.get();
	}
	public String mshahara() {
		return mshahara.get();
	}
	public String tsd() {
		return tsd.get();
	}
	public String kuzaliwa() {
		return kuzaliwa.get();
	}
	public String ajira() {
		return ajira.get();
	}
	public String thibitishwa() {
		return thibitishwa.get();
	}
	public String daraja() {
		return daraja.get();
	}
	public String elimu() {
		return elimu.get();
	}
	public String chuo() {
		return chuo.get();
	}
	public String aliomaliza() {
		return aliomaliza.get();
	}
	public String sasaKazi() {
		return sasaKazi.get();
	}
	public String awaliKazi() {
		return awaliKazi.get();
	}
	public String dini() {
		return dini.get();
	}
	public String alipozaliwa() {
		return alipozaliwa.get();
	}
	public String number() {
		return number.get();
	}
	public String somo1() {
		return somo1.get();
	}
	public String somo2() {
		return somo2.get();
	}
	public String kata() {
		return kata.get();
	}

	
	
	
	
	
	 public StringProperty checkProperty() {
		   return check;
	   }
		
		public StringProperty fNameProperty() {
			return fName;
		}


		public StringProperty mNameProperty() {
			return mName;
		}


		public StringProperty lNameProperty() {
			return lName;
		}


		public StringProperty sexProperty() {
			return sex;
		}


		public StringProperty cheoProperty() {
			return cheo;
		}


		public StringProperty mshaharaProperty() {
			return mshahara;
		}


		public StringProperty tsdProperty() {
			return tsd;
		}


		public StringProperty kuzaliwaProperty() {
			return kuzaliwa;
		}


		public StringProperty ajiraProperty() {
			return ajira;
		}


		public StringProperty thibitishwaProperty() {
			return thibitishwa;
		}


		public StringProperty darajaProperty() {
			return daraja;
		}


		public StringProperty elimuProperty() {
			return elimu;
		}


		public StringProperty chuoProperty() {
			return chuo;
		}


		public StringProperty aliomalizaProperty() {
			return aliomaliza;
		}


		public StringProperty sasaKaziProperty() {
			return sasaKazi;
		}


		public StringProperty awaliKaziProperty() {
			return awaliKazi;
		}


		public StringProperty diniProperty() {
			return dini;
		}


		public StringProperty alipozaliwaProperty() {
			return alipozaliwa;
		}


		public StringProperty numberProperty() {
			return number;
		}


		public StringProperty somo1Property() {
			return somo1;
		}


		public StringProperty somo2Property() {
			return somo2;
		}


		public StringProperty kataProperty() {
			return kata;
		}

		
		
	

}
