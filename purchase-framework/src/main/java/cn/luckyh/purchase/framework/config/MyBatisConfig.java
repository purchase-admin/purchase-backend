package cn.luckyh.purchase.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis支持*匹配扫描包
 */
@Configuration
@MapperScan("cn.luckyh.purchase.**.mapper")
public class MyBatisConfig {
//    @Autowired
//    private Environment env;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//    @Value("${spring.datasource.password}")
//    private String password;
//    @Value("${spring.datasource.driverClassName}")
//    private String driverclass;
//    @Value("${spring.datasource.url}")
//    private String url;

//    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

//    public static String setTypeAliasesPackage(String typeAliasesPackage) {
//        ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
//        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
//        List<String> allResult = new ArrayList<String>();
//        try {
//            for (String aliasesPackage : typeAliasesPackage.split(",")) {
//                List<String> result = new ArrayList<String>();
//                aliasesPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
//                        + ClassUtils.convertClassNameToResourcePath(aliasesPackage.trim()) + "/" + DEFAULT_RESOURCE_PATTERN;
//                Resource[] resources = resolver.getResources(aliasesPackage);
//                if (resources != null && resources.length > 0) {
//                    MetadataReader metadataReader = null;
//                    for (Resource resource : resources) {
//                        if (resource.isReadable()) {
//                            metadataReader = metadataReaderFactory.getMetadataReader(resource);
//                            try {
//                                result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName());
//                            } catch (ClassNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//                if (result.size() > 0) {
//                    HashSet<String> hashResult = new HashSet<String>(result);
//                    allResult.addAll(hashResult);
//                }
//            }
//            if (allResult.size() > 0) {
//                typeAliasesPackage = String.join(",", (String[]) allResult.toArray(new String[0]));
//            } else {
//                throw new RuntimeException("mybatis typeAliasesPackage 路径扫描错误,参数typeAliasesPackage:" + typeAliasesPackage + "未找到任何包");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return typeAliasesPackage;
//    }
//
//    public Resource[] resolveMapperLocations(String[] mapperLocations) {
//        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
//        List<Resource> resources = new ArrayList<Resource>();
//        if (mapperLocations != null) {
//            for (String mapperLocation : mapperLocations) {
//                try {
//                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
//                    resources.addAll(Arrays.asList(mappers));
//                } catch (IOException e) {
//                    // ignore
//                }
//            }
//        }
//        return resources.toArray(new Resource[resources.size()]);
//    }

//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        String typeAliasesPackage = env.getProperty("mybatis.typeAliasesPackage");
//        String mapperLocations = env.getProperty("mybatis.mapperLocations");
//        String configLocation = env.getProperty("mybatis.configLocation");
//        typeAliasesPackage = setTypeAliasesPackage(typeAliasesPackage);
//        VFS.addImplClass(SpringBootVFS.class);
//
//        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
//        sessionFactory.setMapperLocations(resolveMapperLocations(StringUtils.split(mapperLocations, ",")));
//        sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
//        return sessionFactory.getObject();
//    }

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }

}