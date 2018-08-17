package group.msg.beans;

import lombok.Data;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

//code from https://www.youtube.com/watch?v=TPz-iZ_CfMo
@Data
@ManagedBean
@SessionScoped
@Named
public class LangChange implements Serializable {

    private String languagecode;






    public Locale getLocale(){
        return(Locale) WebHelper.getSession().getAttribute("locale");
    }


    private  Map<String,Object> countries;

   {
        Locale roLocale = new Locale.Builder().setLanguage("ro").setRegion("RO").build();
        countries=new LinkedHashMap<String, Object>();
        countries.put("English", Locale.ENGLISH);
        countries.put("Romanian", roLocale);

    }
    public void countryLocaleCodeChanged(ValueChangeEvent take_event){
        String new_language_code=take_event.getNewValue().toString();

        for(Map.Entry<String,Object>entry:countries.entrySet()){
            if(entry.getValue().toString().equals(new_language_code)){
                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale)entry.getValue());

                WebHelper.getSession().setAttribute("locale",entry.getValue());
            }
        }
    }

}
