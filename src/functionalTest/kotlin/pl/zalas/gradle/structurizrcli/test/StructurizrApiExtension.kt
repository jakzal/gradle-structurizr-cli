package pl.zalas.gradle.structurizrcli.test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.junit.jupiter.api.extension.*

class StructurizrApiExtension : BeforeEachCallback, AfterEachCallback, ParameterResolver {
    lateinit var structurizrApi: WireMockServer

    override fun beforeEach(context: ExtensionContext) {
        structurizrApi = WireMockServer(WireMockConfiguration.options().port(9090))
        structurizrApi.start()
    }

    override fun afterEach(context: ExtensionContext) {
        structurizrApi.stop()
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
            parameterContext.parameter.type == WireMockServer::class.java

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any =
            structurizrApi

}