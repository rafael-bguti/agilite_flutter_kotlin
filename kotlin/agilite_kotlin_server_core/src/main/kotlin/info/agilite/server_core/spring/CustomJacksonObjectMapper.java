package info.agilite.server_core.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import info.agilite.shared.json.JsonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class CustomJacksonObjectMapper {
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

		builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
		builder.serializationInclusion(JsonInclude.Include.NON_NULL);

		builder.modulesToInstall(
				JsonUtils.INSTANCE.createLocalDateModule(),
				JsonUtils.INSTANCE.createAttNotLoadedModule(),
				new KotlinModule.Builder().build()
		);

		return builder;		
	}

}

