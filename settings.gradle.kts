rootProject.name = "AI-Powered Movie Sentiment Rating System"

include(
    "applications:frontend-server",
    "applications:data-analyzer-server",
    "applications:data-collector-server",

    "components:data-collector",
    "components:data-analyzer",

    "support:logging-support",
    "support:workflow-support"
)
