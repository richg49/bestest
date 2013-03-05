package hu.promarkvf.besttest;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableUser implements Parcelable {
	private User user;

	public User getUser() {
		return user;
	}

	public ParcelableUser(User user) {
		super();
		this.user = user;
	}

	private ParcelableUser(Parcel in) {
		user = new User();
		user.setName(in.readString());
		user.setRname(in.readString());
		user.setKey(in.readString());
		user.setDbid(in.readLong());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(user.getName());
		parcel.writeString(user.getRname());
		parcel.writeString(user.getKey());
		parcel.writeLong(user.getDbid());
	}

	public static final Parcelable.Creator<ParcelableUser> CREATOR = new Parcelable.Creator<ParcelableUser>() {
		public ParcelableUser createFromParcel(Parcel in) {
			return new ParcelableUser(in);
		}

		public ParcelableUser[] newArray(int size) {
			return new ParcelableUser[size];
		}
	};
}
