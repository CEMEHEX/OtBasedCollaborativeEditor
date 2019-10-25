package ot.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Validator
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.ViewResolverRegistry
import javax.annotation.Resource


@Configuration
@EnableWebFlux
class WebFluxConfig : WebFluxConfigurer {

    @Resource
    private lateinit var thymeleafReactiveViewResolver: ThymeleafReactiveViewResolver

    @Autowired
    private val defaultValidator: Validator? = null

    override fun getValidator(): Validator? {
        return defaultValidator
    }

    override fun configureViewResolvers(registry: ViewResolverRegistry) {
        registry.viewResolver(thymeleafReactiveViewResolver)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(false)
    }
}