package dev.enderman.minecraft.plugins.fire.better

import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class InflamityPluginTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: InflamityPlugin

    @BeforeTest fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.loadWith(InflamityPlugin::class.java, "paper-plugin.yml")

        MockBukkit.ensureMocking()
    }

    @Test fun onEnableTest() {
        assertNotNull(server)
        assertNotNull(plugin)
    }

    @AfterTest fun tearDown() {
        MockBukkit.unmock()
    }
}
