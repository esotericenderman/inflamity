package dev.enderman.minecraft.plugins.fire.better

import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

abstract class AbstractInflamityPluginTest {
    protected lateinit var server: ServerMock
    protected lateinit var plugin: InflamityPlugin

    @BeforeTest fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.loadWith(InflamityPlugin::class.java, "paper-plugin.yml")
    }

    @Test fun `server should be mocked correctly`() {
        MockBukkit.ensureMocking()
    }

    @Test fun `server should start without errors`() {
        assertNotNull(server)
    }

    @Test fun `plugin should enable without errors`() {
        assertNotNull(plugin)
    }

    @AfterTest fun tearDown() {
        MockBukkit.unmock()
    }
}
