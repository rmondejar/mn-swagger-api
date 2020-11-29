package mn.swagger.api.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class BarDto {

    public BarDto(String id,  String label, long seconds) {
        this.id = id;
        this.label = label;
        this.seconds = seconds;
    }

    @NotBlank
    @NotNull
    private String id;

    @NotBlank
    @NotNull
    private String label;

    @NotNull
    @Min(1606593593)
    private long seconds;

    public String getId() { return id; }

    public String getLabel() { return label; }

    public void setLabel(String label) { this.label = label; }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) { this.seconds = seconds; }
}
