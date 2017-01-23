package olegkuro.learnbyear.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Roman on 21/01/2017.
 * used to store values in SharedPreferences. Keys are language names
 */

public class ParcelablePair implements Parcelable {
    /**
     * language ISO-639/1 code
     */
    private String code;
    /**
     * true if user has chosen the language as native
     */
    private boolean isChosenNative;

    /**
     * true if user learns the language
     */
    private boolean isChosenLearn;

    private ParcelablePair(Parcel parcel) {
        code = parcel.readString();
        try {
            boolean [] booleen = new boolean[1];
            parcel.readBooleanArray(booleen);
            isChosenNative = booleen[0];
            parcel.readBooleanArray(booleen);
            isChosenLearn = booleen[0];
        } catch (IndexOutOfBoundsException e) {

        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        boolean[] booleanArray = {isChosenNative};
        parcel.writeBooleanArray(booleanArray);
        boolean[] aBooleanArray = {isChosenLearn};
        parcel.writeBooleanArray(aBooleanArray);
    }

    public static Creator<ParcelablePair> CREATOR = new Creator<ParcelablePair>() {
        @Override
        public ParcelablePair createFromParcel(Parcel parcel) {
            return new ParcelablePair(parcel);
        }

        @Override
        public ParcelablePair[] newArray(int size) {
            return new ParcelablePair[size];
        }
    };
}
