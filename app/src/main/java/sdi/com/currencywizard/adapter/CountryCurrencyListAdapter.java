package sdi.com.currencywizard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.model.Country;


public class CountryCurrencyListAdapter extends BaseAdapter {

	private Context context;
	List<Country> countries;
	LayoutInflater inflater;

	private int getResId(String drawableName) {

		try {
			Class<R.drawable> res = R.drawable.class;
			Field field = res.getField(drawableName);
			int drawableId = field.getInt(null);
			return drawableId;
		} catch (Exception e) {
			Log.e("CountryCodePicker", "Failure to get drawable id.", e);
		}
		return -1;
	}

	public CountryCurrencyListAdapter(Context context, List<Country> countries) {
		super();
		this.context = context;
		this.countries = countries;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return countries.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View cellView = convertView;
		Cell cell;
		Country country = countries.get(position);


			if (convertView == null) {
				cell = new Cell();
				cellView = inflater.inflate(R.layout.row, null);
				cell.textView = (TextView) cellView.findViewById(R.id.row_title);
				cell.imageView = (ImageView) cellView.findViewById(R.id.row_icon);
				cellView.setTag(cell);
			} else {
				cell = (Cell) cellView.getTag();
			}

			cell.textView.setText(country.getName());/*country.getName or getCode or dialCode*/


			String codevalue = country.getCode();

			String drawableName = "flag_" + codevalue.substring(0, 2).toLowerCase(Locale.ENGLISH);

			if (drawableName.equals("flag_eu")) drawableName = "euro";

		cell.imageView.setImageResource(getResId(drawableName));

		return cellView;
	}

	static class Cell {
		public TextView textView;
		public ImageView imageView;
	}

	public static final class drawable {
		public static int euro=0x7f020000;
		public static int flag_ac=0x7f020001;
		public static int flag_ad=0x7f020002;
		public static int flag_ae=0x7f020003;
		public static int flag_af=0x7f020004;
		public static int flag_ag=0x7f020005;
		public static int flag_ai=0x7f020006;
		public static int flag_al=0x7f020007;
		public static int flag_am=0x7f020008;
		public static int flag_an=0x7f020009;
		public static int flag_ao=0x7f02000a;
		public static int flag_aq=0x7f02000b;
		public static int flag_ar=0x7f02000c;
		public static int flag_as=0x7f02000d;
		public static int flag_at=0x7f02000e;
		public static int flag_au=0x7f02000f;
		public static int flag_aw=0x7f020010;
		public static int flag_ax=0x7f020011;
		public static int flag_az=0x7f020012;
		public static int flag_ba=0x7f020013;
		public static int flag_bb=0x7f020014;
		public static int flag_bd=0x7f020015;
		public static int flag_be=0x7f020016;
		public static int flag_bf=0x7f020017;
		public static int flag_bg=0x7f020018;
		public static int flag_bh=0x7f020019;
		public static int flag_bi=0x7f02001a;
		public static int flag_bj=0x7f02001b;
		public static int flag_bm=0x7f02001c;
		public static int flag_bn=0x7f02001d;
		public static int flag_bo=0x7f02001e;
		public static int flag_br=0x7f02001f;
		public static int flag_bs=0x7f020020;
		public static int flag_bt=0x7f020021;
		public static int flag_bv=0x7f020022;
		public static int flag_bw=0x7f020023;
		public static int flag_by=0x7f020024;
		public static int flag_bz=0x7f020025;
		public static int flag_ca=0x7f020026;
		public static int flag_cc=0x7f020027;
		public static int flag_cd=0x7f020028;
		public static int flag_cf=0x7f020029;
		public static int flag_cg=0x7f02002a;
		public static int flag_ch=0x7f02002b;
		public static int flag_ci=0x7f02002c;
		public static int flag_ck=0x7f02002d;
		public static int flag_cl=0x7f02002e;
		public static int flag_cm=0x7f02002f;
		public static int flag_cn=0x7f020030;
		public static int flag_co=0x7f020031;
		public static int flag_cr=0x7f020032;
		public static int flag_cu=0x7f020033;
		public static int flag_cv=0x7f020034;
		public static int flag_cx=0x7f020035;
		public static int flag_cy=0x7f020036;
		public static int flag_cz=0x7f020037;
		public static int flag_de=0x7f020038;
		public static int flag_dj=0x7f020039;
		public static int flag_dk=0x7f02003a;
		public static int flag_dm=0x7f02003b;
		public static int flag_do=0x7f02003c;
		public static int flag_dz=0x7f02003d;
		public static int flag_ec=0x7f02003e;
		public static int flag_ee=0x7f02003f;
		public static int flag_eg=0x7f020040;
		public static int flag_eh=0x7f020041;
		public static int flag_er=0x7f020042;
		public static int flag_es=0x7f020043;
		public static int flag_et=0x7f020044;
		public static int flag_fi=0x7f020045;
		public static int flag_fj=0x7f020046;
		public static int flag_fk=0x7f020047;
		public static int flag_fm=0x7f020048;
		public static int flag_fo=0x7f020049;
		public static int flag_fr=0x7f02004a;
		public static int flag_fx=0x7f02004b;
		public static int flag_ga=0x7f02004c;
		public static int flag_gb=0x7f02004d;
		public static int flag_gd=0x7f02004e;
		public static int flag_ge=0x7f02004f;
		public static int flag_gf=0x7f020050;
		public static int flag_gg=0x7f020051;
		public static int flag_gh=0x7f020052;
		public static int flag_gi=0x7f020053;
		public static int flag_gl=0x7f020054;
		public static int flag_gm=0x7f020055;
		public static int flag_gn=0x7f020056;
		public static int flag_gp=0x7f020057;
		public static int flag_gq=0x7f020058;
		public static int flag_gr=0x7f020059;
		public static int flag_gs=0x7f02005a;
		public static int flag_gt=0x7f02005b;
		public static int flag_gu=0x7f02005c;
		public static int flag_gw=0x7f02005d;
		public static int flag_gy=0x7f02005e;
		public static int flag_hk=0x7f02005f;
		public static int flag_hm=0x7f020060;
		public static int flag_hn=0x7f020061;
		public static int flag_hr=0x7f020062;
		public static int flag_ht=0x7f020063;
		public static int flag_hu=0x7f020064;
		public static int flag_id=0x7f020065;
		public static int flag_ie=0x7f020066;
		public static int flag_il=0x7f020067;
		public static int flag_im=0x7f020068;
		public static int flag_in=0x7f020069;
		public static int flag_io=0x7f02006a;
		public static int flag_iq=0x7f02006b;
		public static int flag_ir=0x7f02006c;
		public static int flag_is=0x7f02006d;
		public static int flag_it=0x7f02006e;
		public static int flag_je=0x7f02006f;
		public static int flag_jm=0x7f020070;
		public static int flag_jo=0x7f020071;
		public static int flag_jp=0x7f020072;
		public static int flag_ke=0x7f020073;
		public static int flag_kg=0x7f020074;
		public static int flag_kh=0x7f020075;
		public static int flag_ki=0x7f020076;
		public static int flag_km=0x7f020077;
		public static int flag_kn=0x7f020078;
		public static int flag_kp=0x7f020079;
		public static int flag_kr=0x7f02007a;
		public static int flag_kw=0x7f02007b;
		public static int flag_ky=0x7f02007c;
		public static int flag_kz=0x7f02007d;
		public static int flag_la=0x7f02007e;
		public static int flag_lb=0x7f02007f;
		public static int flag_lc=0x7f020080;
		public static int flag_li=0x7f020081;
		public static int flag_lk=0x7f020082;
		public static int flag_lr=0x7f020083;
		public static int flag_ls=0x7f020084;
		public static int flag_lt=0x7f020085;
		public static int flag_lu=0x7f020086;
		public static int flag_lv=0x7f020087;
		public static int flag_ly=0x7f020088;
		public static int flag_ma=0x7f020089;
		public static int flag_mc=0x7f02008a;
		public static int flag_md=0x7f02008b;
		public static int flag_me=0x7f02008c;
		public static int flag_mf=0x7f02008d;
		public static int flag_mg=0x7f02008e;
		public static int flag_mh=0x7f02008f;
		public static int flag_mk=0x7f020090;
		public static int flag_ml=0x7f020091;
		public static int flag_mm=0x7f020092;
		public static int flag_mn=0x7f020093;
		public static int flag_mo=0x7f020094;
		public static int flag_mp=0x7f020095;
		public static int flag_mq=0x7f020096;
		public static int flag_mr=0x7f020097;
		public static int flag_ms=0x7f020098;
		public static int flag_mt=0x7f020099;
		public static int flag_mu=0x7f02009a;
		public static int flag_mv=0x7f02009b;
		public static int flag_mw=0x7f02009c;
		public static int flag_mx=0x7f02009d;
		public static int flag_my=0x7f02009e;
		public static int flag_mz=0x7f02009f;
		public static int flag_na=0x7f0200a0;
		public static int flag_nc=0x7f0200a1;
		public static int flag_ne=0x7f0200a2;
		public static int flag_nf=0x7f0200a3;
		public static int flag_ng=0x7f0200a4;
		public static int flag_ni=0x7f0200a5;
		public static int flag_nl=0x7f0200a6;
		public static int flag_no=0x7f0200a7;
		public static int flag_np=0x7f0200a8;
		public static int flag_nr=0x7f0200a9;
		public static int flag_nu=0x7f0200aa;
		public static int flag_nz=0x7f0200ab;
		public static int flag_om=0x7f0200ac;
		public static int flag_pa=0x7f0200ad;
		public static int flag_pe=0x7f0200ae;
		public static int flag_pf=0x7f0200af;
		public static int flag_pg=0x7f0200b0;
		public static int flag_ph=0x7f0200b1;
		public static int flag_pk=0x7f0200b2;
		public static int flag_pl=0x7f0200b3;
		public static int flag_pm=0x7f0200b4;
		public static int flag_pn=0x7f0200b5;
		public static int flag_pr=0x7f0200b6;
		public static int flag_ps=0x7f0200b7;
		public static int flag_pt=0x7f0200b8;
		public static int flag_pw=0x7f0200b9;
		public static int flag_py=0x7f0200ba;
		public static int flag_qa=0x7f0200bb;
		public static int flag_re=0x7f0200bc;
		public static int flag_ro=0x7f0200bd;
		public static int flag_rs=0x7f0200be;
		public static int flag_ru=0x7f0200bf;
		public static int flag_rw=0x7f0200c0;
		public static int flag_sa=0x7f0200c1;
		public static int flag_sb=0x7f0200c2;
		public static int flag_sc=0x7f0200c3;
		public static int flag_sd=0x7f0200c4;
		public static int flag_se=0x7f0200c5;
		public static int flag_sg=0x7f0200c6;
		public static int flag_sh=0x7f0200c7;
		public static int flag_si=0x7f0200c8;
		public static int flag_sj=0x7f0200c9;
		public static int flag_sk=0x7f0200ca;
		public static int flag_sl=0x7f0200cb;
		public static int flag_sm=0x7f0200cc;
		public static int flag_sn=0x7f0200cd;
		public static int flag_so=0x7f0200ce;
		public static int flag_sr=0x7f0200cf;
		public static int flag_st=0x7f0200d0;
		public static int flag_sv=0x7f0200d1;
		public static int flag_sy=0x7f0200d2;
		public static int flag_sz=0x7f0200d3;
		public static int flag_tc=0x7f0200d4;
		public static int flag_td=0x7f0200d5;
		public static int flag_tf=0x7f0200d6;
		public static int flag_tg=0x7f0200d7;
		public static int flag_th=0x7f0200d8;
		public static int flag_tj=0x7f0200d9;
		public static int flag_tk=0x7f0200da;
		public static int flag_tl=0x7f0200db;
		public static int flag_tm=0x7f0200dc;
		public static int flag_tn=0x7f0200dd;
		public static int flag_to=0x7f0200de;
		public static int flag_tr=0x7f0200df;
		public static int flag_tt=0x7f0200e0;
		public static int flag_tv=0x7f0200e1;
		public static int flag_tw=0x7f0200e2;
		public static int flag_tz=0x7f0200e3;
		public static int flag_ua=0x7f0200e4;
		public static int flag_ug=0x7f0200e5;
		public static int flag_um=0x7f0200e6;
		public static int flag_us=0x7f0200e7;
		public static int flag_uy=0x7f0200e8;
		public static int flag_uz=0x7f0200e9;
		public static int flag_va=0x7f0200ea;
		public static int flag_vc=0x7f0200eb;
		public static int flag_ve=0x7f0200ec;
		public static int flag_vg=0x7f0200ed;
		public static int flag_vi=0x7f0200ee;
		public static int flag_vn=0x7f0200ef;
		public static int flag_vu=0x7f0200f0;
		public static int flag_wf=0x7f0200f1;
		public static int flag_ws=0x7f0200f2;
		public static int flag_xa=0x7f0200f3;
		public static int flag_xk=0x7f0200f4;
		public static int flag_xo=0x7f0200f5;
		public static int flag_ye=0x7f0200f6;
		public static int flag_yt=0x7f0200f7;
		public static int flag_yu=0x7f0200f8;
		public static int flag_za=0x7f0200f9;
		public static int flag_zm=0x7f0200fa;
		public static int flag_zw=0x7f0200fb;
		public static int flog_at=0x7f0200fc;
		public static int search_icon=0x7f0200fd;
	}


}