package livraria.controle.web.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import dominio.EntidadeDominio;

@FacesConverter(value = "genericConverter")    
public class GenericConverter implements Converter {
  @Override
  public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
      if (value != null && !value.isEmpty()) {
          return (EntidadeDominio) uiComponent.getAttributes().get(value);
      }
      return null;
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
      if (value instanceof EntidadeDominio) {
    	  EntidadeDominio entity= (EntidadeDominio) value;
          if (entity.getId() != null && entity instanceof EntidadeDominio && entity.getId() != 0) {
              uiComponent.getAttributes().put( Integer.valueOf(entity.getId()).toString(), entity);
              return Integer.valueOf(entity.getId()).toString();
          }
      }
      return "";
  }
}