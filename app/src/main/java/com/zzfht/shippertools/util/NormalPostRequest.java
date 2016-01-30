package com.zzfht.shippertools.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by think on 2015-11-13.
 */
public class NormalPostRequest extends Request<JSONObject> {

    private Map<String,String> mMap;
    private Listener<JSONObject> mListener;

    public NormalPostRequest(String url, Listener listener, Response.ErrorListener errorListener, Map<String, String> map) {
        super(Method.POST,url,errorListener);

        mListener = listener;

        mMap = map;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError());
        } catch (JSONException e) {
            return Response.error(new ParseError());
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}
