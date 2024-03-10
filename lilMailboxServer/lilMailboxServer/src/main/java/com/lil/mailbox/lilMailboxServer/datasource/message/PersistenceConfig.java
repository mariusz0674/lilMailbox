//package com.lil.mailbox.lilMailboxServer.datesource.message;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//
//import javax.sql.DataSource;
//
//public class PersistenceConfig {
//    @Configuration
//    @MapperScan("com.baeldung.mybatis")
//    public class PersistenceConfig {
//
//        @Bean
//        public DataSource dataSource() {
//            return new EmbeddedDatabaseBuilder()
//                    .setType(EmbeddedDatabaseType.H2)
//                    .addScript("schema.sql")
//                    .addScript("data.sql")
//                    .build();
//        }
//
//        @Bean
//        public SqlSessionFactory sqlSessionFactory() throws Exception {
//            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//            factoryBean.setDataSource(dataSource());
//            return factoryBean.getObject();
//        }
//    }
//}
