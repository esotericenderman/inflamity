rootProject.name = "inflamity"

include("project")

exec {
    commandLine("bash", "./scripts/export_logo.sh")
}
