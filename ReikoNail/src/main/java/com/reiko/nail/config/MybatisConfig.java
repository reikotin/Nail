package com.reiko.nail.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;


@Configuration
@MapperScan("com.reiko.nail.dao")
public class MybatisConfig {
	
//	@Bean
//	public DataSource dataSource() {
//		return DataSourceBuilder.create().url("jdbc:mysql://localhost:3306/rnail").build();
//		
//	}

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
        factory.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
        factory.setMapperLocations(resolver.getResources("classpath:com/reiko/nail/dao/*.xml"));
        
        return factory.getObject();
    }
}
