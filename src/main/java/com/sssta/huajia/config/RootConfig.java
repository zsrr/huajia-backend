package com.sssta.huajia.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jsms.api.JSMSClient;
import cn.jsms.api.common.JSMSConfig;
import com.sssta.huajia.Constants;
import com.sssta.huajia.util.PropertiesUtils;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;


@Configuration
@ComponentScan(basePackages =
        {"com.sssta.huajia.dao", "com.sssta.huajia.service"})
@EnableCaching
@EnableTransactionManagement
@EnableAsync
public class RootConfig {

    @Bean(name = "jedisPool")
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setBlockWhenExhausted(true);
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        config.setMaxTotal(32);
        config.setMaxIdle(32);
        config.setMaxWaitMillis(300 * 1000);
        config.setMinEvictableIdleTimeMillis(15 * 60 * 1000);
        return new JedisPool(config, "127.0.0.1", 6379, 20 * 1000);
    }

    @Bean
    public JSMSClient jsmsClient() {
        Properties properties = PropertiesUtils.loadProperties("jiguang.properties");
        return new JSMSClient(properties.getProperty("masterSecret"), properties.getProperty("appKey"), null, JSMSConfig.getInstance());
    }

    @Bean
    public JPushClient jPushClient() {
        Properties properties = PropertiesUtils.loadProperties("jiguang.properties");

        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setApnsProduction(false);
        clientConfig.setConnectionTimeout(10 * 1000);
        clientConfig.setMaxRetryTimes(5);

        return new JPushClient(properties.getProperty("masterSecret"),
                properties.getProperty("appKey"), null, clientConfig);
    }

    /*@Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean ehcache = new EhCacheManagerFactoryBean();
        ehcache.setConfigLocation(new ClassPathResource("ehcache.xml"));
        ehcache.setCacheManagerName("spring-cache");
        return ehcache;
    }*/

    /*@Bean
    public EhCacheCacheManager cacheManager(CacheManager cacheManager) {
        return new EhCacheCacheManager(cacheManager);
    }*/

    public static class HibernatePropertiesConfig {
        private final String dialect;
        private final String hbm2ddl_auto;

        public HibernatePropertiesConfig(String dialect, String hbm2ddl_auto) {
            this.dialect = dialect;
            this.hbm2ddl_auto = hbm2ddl_auto;
        }

        public String getDialect() {
            return dialect;
        }

        public String hbm2ddl_auto() {
            return hbm2ddl_auto;
        }
    }

    @Bean
    public DataSource mysqlDataSource() {
        DataSource dataSource;
        JndiTemplate jndi = new JndiTemplate();
        try {
            dataSource = jndi.lookup("java:comp/env/jdbc/huajia", DataSource.class);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    /*@Bean
    @Profile("dev")
    public DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.H2).
                setName("zhihu").
                build();
    }*/

    @Bean
    LocalSessionFactoryBean factoryBean(DataSource dataSource, HibernatePropertiesConfig propertiesConfig) {
        LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
        sfb.setDataSource(dataSource);
        sfb.setAnnotatedPackages("com.sssta.huajia.domain");
        sfb.setPackagesToScan("com.sssta.huajia.domain");
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        properties.put("hibernate.dialect", propertiesConfig.getDialect());
        properties.put("hibernate.hbm2ddl.auto", propertiesConfig.hbm2ddl_auto());
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        //properties.put("hibernate.cache.use_second_level_cache", "true");
        //properties.put("hibernate.cache.use_query_cache", "true");
        //properties.put("hibernate.cache.region.factory_class", EhCacheRegionFactory.class.getName());
        //properties.put("net.sf.ehcache.configurationResourceName", "ehcache.xml");
        //properties.put("hibernate.cache.use_structured_entries", "false");
        //properties.put("hibernate.generate_statistics", "true");
        sfb.setHibernateProperties(properties);
        return sfb;
    }

    @Bean
    public HibernatePropertiesConfig productionDialect() {
        return new HibernatePropertiesConfig("org.hibernate.dialect.MySQL57Dialect", "update");
    }

    /*@Bean
    @Profile("dev")
    public HibernatePropertiesConfig testDialect() {
        return new HibernatePropertiesConfig("org.hibernate.dialect.H2Dialect", "create");
    }*/

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    // elasticsearch configuration
    // 远程连接集群
    /*@Bean(destroyMethod = "close")
    @Profile("pro")
    public Client transportClient() {
        TransportClient client = null;
        try {
            client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Bean(destroyMethod = "close")
    @Profile("dev")
    public Client nodeClient() {
        Settings settings = Settings.settingsBuilder().
                put("http.enabled", false).
                put("path.home", "/Users/zhangshirui/elasticsearch-2.4.0").
                put("path.data", "/Users/zhangshirui/zhihu-search-data").
                build();
        Node node = nodeBuilder().settings(settings).local(true).node();
        return node.client();
    }*/

    /*@Bean
    public ElasticsearchOperations elasticsearchTemplate(Client client) {
        return new ElasticsearchTemplate(client);
    }*/

    @Bean(name = Constants.ASYNC_EXECUTOR)
    public Executor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(200);
        return executor;
    }
}
