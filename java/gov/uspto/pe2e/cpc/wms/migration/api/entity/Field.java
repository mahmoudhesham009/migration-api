package gov.uspto.pe2e.cpc.wms.migration.api.entity;

import java.util.Objects;

import org.json.JSONArray;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Field {

	private String id;
	private String name;
	private String value;
	private boolean isRequired;
	private String type;
	private JSONArray options;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, isRequired, name, value);
	}

}
