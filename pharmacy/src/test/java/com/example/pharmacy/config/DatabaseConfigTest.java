/*
package com.example.pharmacy.config;

import org.junit.jupiter.api.Test;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.OracleDialect;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConfigTest {

    private final DatabaseConfig config = new DatabaseConfig();

    @Test
    void jdbcCustomConversions_deberiaIncluirConvertersPersonalizados() throws Exception {
        JdbcCustomConversions conversions = config.jdbcCustomConversions();

        assertNotNull(conversions);

        // Accedemos al campo privado 'converters' usando reflexión
        Field convertersField = JdbcCustomConversions.class.getDeclaredField("converters");
        convertersField.setAccessible(true);
        Iterable<?> convertersList = (Iterable<?>) convertersField.get(conversions);

        boolean tieneBooleanConverter = false;
        boolean tieneBooleanToStringConverter = false;

        for (Object c : convertersList) {
            String simpleName = c.getClass().getSimpleName();
            if ("BooleanConverter".equals(simpleName)) {
                tieneBooleanConverter = true;
            }
            if ("BooleanToStringConverter".equals(simpleName)) {
                tieneBooleanToStringConverter = true;
            }
        }

        assertTrue(tieneBooleanConverter, "Debe contener BooleanConverter");
        assertTrue(tieneBooleanToStringConverter, "Debe contener BooleanToStringConverter");
    }

    @Test
    void jdbcDialect_deberiaRetornarOracleDialect() {
        Dialect dialect = config.jdbcDialect(null); // el argumento no se usa realmente en tu método
        assertNotNull(dialect);
        assertEquals(OracleDialect.INSTANCE, dialect);
    }

    @Test
    void namingStrategy_deberiaRetornarNombresDeColumnasEnMayusculas() {
        NamingStrategy namingStrategy = config.namingStrategy();

        assertNotNull(namingStrategy);

        // Mock para RelationalPersistentProperty
        RelationalPersistentProperty propiedad = Mockito.mock(RelationalPersistentProperty.class);
        Mockito.when(propiedad.getName()).thenReturn("miCampo");

        String nombreColumna = namingStrategy.getColumnName(propiedad);
        assertEquals("MICAMPO", nombreColumna);

        // También comprobamos getTableName
        String tableName = namingStrategy.getTableName(Object.class);
        assertNotNull(tableName);
    }
}
*/