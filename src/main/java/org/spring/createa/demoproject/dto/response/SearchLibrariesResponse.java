package org.spring.createa.demoproject.dto.response;

import java.util.List;
import org.spring.createa.demoproject.dto.Library;

public record SearchLibrariesResponse(Response response) {

  public record Response(List<Doc> libs) {

  }

  public record Doc(Library lib) {

  }

}
