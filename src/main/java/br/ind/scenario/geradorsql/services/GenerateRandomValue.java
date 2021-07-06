package br.ind.scenario.geradorsql.services;

import br.ind.scenario.geradorsql.exceptions.GenerationValuesException;
import br.ind.scenario.geradorsql.models.config.MapFaker;
import br.ind.scenario.geradorsql.models.table.Field;
import br.ind.scenario.geradorsql.models.table.FieldParams;
import br.ind.scenario.geradorsql.models.table.TableValue;
import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;

public class GenerateRandomValue {

    @NotNull
    private final Faker faker;
    @NotNull
    private final MapFaker[] mapFakers;

    public GenerateRandomValue(@NotNull Path pathMapFakers) throws IOException {
        faker = new Faker();
        mapFakers = Serializer.deserializer(pathMapFakers, MapFaker[].class).orElseThrow();
    }

    public String generateValue(@NotNull Field field) throws GenerationValuesException {
        Optional<String> methodName = getMethodReferenceInConfiguration(field.faker());
        if (methodName.isEmpty()) throw new GenerationValuesException("Field haven't value to convert faker");

        try {
            Object objReturned = faker.getClass().getMethod(methodName.get()).invoke(faker);
            if (field.methodParams().length > 0) {
                Object[] values = Arrays.stream(field.methodParams()).map(FieldParams::value).toArray();
                List<Class<?>> classes = new ArrayList<>();
                for (FieldParams fieldParams : field.methodParams())
                    classes.add(fieldParams.getClassTotalPackage());

                return objReturned.getClass().getMethod(field.faker(), classes.toArray(Class[]::new)).invoke(objReturned, values).toString();
            }
            return objReturned.getClass().getMethod(field.faker()).invoke(objReturned).toString();
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new GenerationValuesException();
        }
    }

    private Optional<String> getMethodReferenceInConfiguration(@NotNull String fakerReference) {
        for (MapFaker reference : mapFakers) {
            if (Arrays.asList(reference.value()).contains(fakerReference))
                return Optional.of(reference.key());
        }
        return Optional.empty();
    }

    public String generateValueRelationship(@NotNull Field field, @NotNull List<TableValue> tables) {
        if (Objects.isNull(field.relationship())) throw new IllegalArgumentException("Relationship not exists!");
        if (field.relationship().isAutoGenerateID()) {
            for (TableValue table : tables)
                if (Objects.equals(table.getName(), field.relationship().table()))
                    return String.valueOf((new Random().nextInt(table.getRows().size()) + 1));
        }
        for (TableValue table : tables) {
            if (Objects.equals(table.getName(), field.relationship().table())) {
                Random random = new Random();
                long randomIndex = random.nextInt(table.getRows().size());

                return table.getRows().stream()
                        .skip(randomIndex).findFirst().orElseThrow()
                        .getValues().stream()
                        .filter(fieldValue -> Objects.equals(fieldValue.getName(), field.relationship().field()))
                        .findFirst().orElseThrow()
                        .getValue().replaceAll("'", "");
            }
        }
        throw new IllegalArgumentException("Relationship not found!");
    }
}
