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
      EntidadeDominio entidade = null;
	  if (value != null && !value.isEmpty()) {
          entidade = (EntidadeDominio) uiComponent.getAttributes().get(value);
      }
      return entidade;
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
      if(value == null) {
    	  return "";
      }
      String id = null;
	  if (value instanceof EntidadeDominio) {
    	  EntidadeDominio entity= (EntidadeDominio) value;
          if (entity.getId() != null && entity.getId() != 0) {
        	  id = entity.getId().toString();
        	  uiComponent.getAttributes().put(id, value);
//              uiComponent.getAttributes().put( Integer.valueOf(entity.getId()).toString(), entity);
//              return Integer.valueOf(entity.getId()).toString();
          }
      }
      return id;
  }
}