SpringMVC简单模拟了IOC的流程与事务细节。<br/>
在扫描指定包后使用反射动态实例化指定注解并一个HashMap维护@Controller,@Service,@Component的实例，key为注解的value，value为实例对象。<br/>
另使用一个HashMap维护@RequestMapping的url与具体方法之间的映射关系。在java.sql.Connection的基础上封装了Spring的事务管理。
