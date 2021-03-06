package noear.weed;

import noear.weed.ext.Act2;
import noear.weed.ext.Fun0;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuety on 15/9/2.
 */
public class DataItemEx implements IDataItem{
    HashMap<String,Fun0<Object>> _data = new HashMap<>();
    boolean _isNotNull = false; //不需要null的数据

    public DataItemEx() { }
    public DataItemEx(boolean isUsingDbNull) { _isUsingDbNull = isUsingDbNull; }

    public int count() {
        return _data.size();
    }
    public void clear() {
        _data.clear();
    }

    public boolean exists(String name) {
        return _data.containsKey(name);
    }

    public Iterable<String> keys() {
        return _data.keySet();
    }


    public IDataItem set(String name, Object value) {
        _data.put(name,(()-> value));
        return this;
    }

    public DataItemEx set(String name, Fun0<Object> value) {
        _data.put(name,value);
        return this;
    }

    public Object get(String name) {
        return _data.get(name);
    }

    public Variate getVariate(String name) {
        if (_data.containsKey(name))
            return new VariateEx(name, _data.get(name));
        else
            return new Variate(name, null);
    }

    public <T extends IBinder> T toItem(T item) {
        item.bind((key) -> getVariate(key));

        return item;
    }


    public short getShort(String name) {
        return (short)get(name);
    }

    public int getInt(String name) {
        return (int)get(name);
    }

    public long getLong(String name) {
        return (long)get(name);
    }

    public double getDouble(String name) {
        return (double)get(name);
    }

    public float getFloat(String name) {
        return (float)get(name);
    }

    public String getString(String name) {
        return (String)get(name);
    }

    public boolean getBoolean(String name) {
        return (boolean)get(name);
    }

    public Date getDateTime(String name) {
        return (Date)get(name);
    }



    //
    //===========================
    //
    public void forEach(Act2<String, Object> callback)
    {
        for(Map.Entry<String,Fun0<Object>> kv : _data.entrySet()) {
            Object val = kv.getValue().run();

            if (val == null && _isUsingDbNull) {
                callback.run(kv.getKey(), "$NULL");
            } else {
                callback.run(kv.getKey(), val);
            }
        }
    }

    private boolean _isUsingDbNull=false;
}
