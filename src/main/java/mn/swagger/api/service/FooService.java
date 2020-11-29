package mn.swagger.api.service;

import mn.swagger.api.dtos.BarDto;

import javax.inject.Singleton;
import java.time.Instant;
import java.util.*;

@Singleton
public class FooService {

    private Map<String, BarDto> barCache = new HashMap<>();

    public BarDto create(String id) {
        BarDto bar = new BarDto(id, decorate(id), currentSeconds());
        barCache.put(id, bar);
        return bar;
    }

    public Optional<BarDto> update(String id) {
        Optional<BarDto> optBar = findById(id);
        return optBar.map( bar -> {
            bar.setLabel(decorate(id));
            bar.setSeconds(currentSeconds());
            return bar;
        });
    }

    public Optional<BarDto> findById(String id) {
        return Optional.ofNullable(barCache.get(id));
    }

    public List<BarDto> findAll() {
        return new ArrayList<>(barCache.values());
    }

    public Optional<BarDto> remove(String id) {
        return Optional.ofNullable(barCache.remove(id));
    }

    private String decorate(String text) {
        List<String> decorators = List.of("***", "---", "|||", "...", "$$$", "!!!");
        int index = new Random().nextInt(decorators.size());
        return decorators.get(index)+" " + text + " "+decorators.get(index);
    }

    private long currentSeconds() {
        return Instant.now().getEpochSecond();
    }
}
