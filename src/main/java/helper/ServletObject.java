package helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.AbstractIncomingVO;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zhongqin on 6/4/17.
 * ServletObject
 */
public class ServletObject {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected long startTime;
    protected String ipAddress;
    protected ObjectMapper mapper;
    protected String request_content;
    protected JSONObject jsonObject;
    protected String statusCode;
    protected String statusMessage;
    protected String statusColour;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ServletObject.class);
    private boolean isLogOn = true;

    public void set(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.startTime = System.currentTimeMillis();

        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        request.getContextPath();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        jsonObject = new JSONObject();
        jsonObject.put("j_session_id",request.getSession(true).getId());
        jsonObject.put("requestURI",request.getRequestURI());
        jsonObject.put("action",request.getParameter("action"));

        ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
    }


    /*
        Mapping incoming Class to AbstractIncomingVO
        Developers to extends the incoming classes to AbstractIncomingVO first
     */
    public AbstractIncomingVO getIncomingVO(Class incomingClass) throws IOException {
        return (AbstractIncomingVO) getIncoming(incomingClass);
    }

    /*
        This method morphs the incomingObject to its specified incomingClass.
     */
    private Object getIncoming(Class incomingClass) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(request.getInputStream()));

        String line = buffer.readLine();
        int length = 0;
        String json = line;
        while( (line != null) && (!line.isEmpty()) ){
            length = length + line.length();
            line = buffer.readLine();
            if(line!=null){
                json = json + line;
            }
        }

        request_content = json;
        return mapper.readValue(json, incomingClass);
    }

    /*
        This method provide a response to the calling client
     */
    public void respond() throws IOException {
        long timeTaken = System.currentTimeMillis() - this.startTime;

        String message =
                "URI:"+request.getRequestURI()+
                        " TimeTaken: "+timeTaken+"ms"+
                        " IP:"+ipAddress +
                        " In: "+request_content +
                        " Out: "+jsonObject;
        if(isLogOn){
            log.info(message);
        }

        String responseString = null;
        if(jsonObject!=null){
             responseString = jsonObject.toString();
        }
        response.setStatus(200);
        jsonObject.write(response.getWriter());
    }

    /*
        This method sets the jsonObject with key and object
     */
    public void setJsonObject(String key, Object object){
        jsonObject.put(key,object);
    }


    public void setStatus(String statusCode, String statusMessage, String statusColour){

        this.statusCode=statusCode;
        this.statusMessage=statusMessage;
        this.statusColour=statusColour;

        jsonObject.put("statusCode",statusCode);
        jsonObject.put("statusMessage",statusMessage);
        jsonObject.put("statusColour",statusColour);

        if("R".equalsIgnoreCase(statusColour)){
            /*
               TODO: setup slackutil to inform
             */
        }
    }

    public void setLogging(boolean isLogOn) {
        this.isLogOn=isLogOn;
    }
}
