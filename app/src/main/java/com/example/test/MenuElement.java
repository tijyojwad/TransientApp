package com.example.test;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joyjitdaw on 1/11/15.
 */
public class MenuElement implements Parcelable {
    static int _itemCount = 0;

    String _itemName;
    float _itemPrice;
    String _url;
    int _id = _itemCount++;
    int _orders = 0;


    public MenuElement(String name, float price, String imageUrl) {
        this._itemName = name;
        this._itemPrice = price;
        this._url = imageUrl;
    }

    public String getName() {
        return _itemName;
    }

    public float getPrice() {
        return _itemPrice;
    }

    public String getURL() {
        return _url;
    }

    public int getId() {
        return _id;
    }

    public void addOne() {
        _orders++;
    }

    public int getOrders() {
        return _orders;
    }

    public void clear() {
        _orders = 0;
    }

    protected MenuElement(Parcel in) {
        _itemCount = in.readInt();
        _itemName = in.readString();
        _itemPrice = in.readFloat();
        _url = in.readString();
        _id = in.readInt();
        _orders = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_itemCount);
        dest.writeString(_itemName);
        dest.writeFloat(_itemPrice);
        dest.writeString(_url);
        dest.writeInt(_id);
        dest.writeInt(_orders);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MenuElement> CREATOR = new Parcelable.Creator<MenuElement>() {
        @Override
        public MenuElement createFromParcel(Parcel in) {
            return new MenuElement(in);
        }

        @Override
        public MenuElement[] newArray(int size) {
            return new MenuElement[size];
        }
    };
}