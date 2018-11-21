package com.bektz.dataplatformsoar.repository;

import com.bektz.dataplatformsoar.repository.domain.SecretColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecretColumnRepository extends JpaRepository<SecretColumn, Long> {

    public SecretColumn getSecretColumnByColumnAndTableAndSchemaName(String column, String table, String schemaName);

    public List<SecretColumn> getSecretColumnsByTableAndSchemaName(String table, String schemaName);
}
