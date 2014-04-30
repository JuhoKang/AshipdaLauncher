package kr.re.ec.ashipdalauncher.constants;

import android.provider.BaseColumns;

//constants of vivadb
public final class SosConstants {

	public SosConstants() {
	}

	public static abstract class SosFrame implements BaseColumns {

		// DB name,version
		public static final String DB_NAME = "Sos";
		public static final int DB_VERSION = 1;
		// table name
		public static final String TABLE_NAME = "sosdb";
		// column name
		public static final String COLUMN_NAME_SOS_ID = "id";
		public static final String COLUMN_NAME_SOS_NAME = "sosname";
		public static final String COLUMN_NAME_SOS_PHONENUM = "sosphonenum";
	}

}


