package info.agilite.server_core.metadata.exceptions

abstract class MetadataNotFoundException(
    val metadataName: String,
    message: String,
) : RuntimeException(message)

class EntityClassNotFoundException(
    entityName: String,
) : MetadataNotFoundException(
    entityName,
    "EntityClass $entityName not found",
)

class EntityMetadataNotFoundException(
    entityName: String,
) : MetadataNotFoundException(
    entityName,
    "Entity $entityName not found",
)

class FieldMetadataNotFoundException(
    fieldName: String,
) : MetadataNotFoundException(
    fieldName,
    "Field $fieldName not found",
)

class AutocompleteMetadataNotFoundException(
    autocompleteName: String,
) : MetadataNotFoundException(
    autocompleteName,
    "Autocomplete $autocompleteName not found",
)

class TaskMetadataNotFoundException(
    taskName: String,
) : MetadataNotFoundException(
    taskName,
    "Task $taskName not found",
)