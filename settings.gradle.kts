rootProject.name = "inflamity"

include("project")

val logo = File("docs/assets/images/logos/inflamity.png")

if (!logo.exists()) {
    exec {
        commandLine("bash", "./scripts/export_logo.sh")

        isIgnoreExitValue = true
    }
}
