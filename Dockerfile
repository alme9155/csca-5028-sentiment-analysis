# ================================
# JRE
# ================================
# ================================
# JRE – Fixed: added java.desktop for FreeMarker
# ================================
FROM eclipse-temurin:21-alpine AS jre-build

RUN $JAVA_HOME/bin/jlink \
    --add-modules java.base,java.logging,java.management,java.naming,java.net.http,java.security.jgss,java.sql,jdk.crypto.ec,jdk.unsupported,java.desktop \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /javaruntime


# ================================
# Runtime Image
# ================================
FROM alpine:3.20

RUN apk add --no-cache ca-certificates tini
RUN addgroup -g 1001 kotlin && adduser -S -D -u 1001 -G kotlin kotlin

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

WORKDIR /app
# RUN mkdir -p /app/models
# COPY components/sentiment/models/distilbert-sst5-finetuned-v3 /app/models/distilbert-sst5-finetuned-v3

RUN mkdir -p /opt/applications && chown kotlin:kotlin /opt/applications

ARG APP_NAME=frontend-server
ENV APP_JAR=/opt/applications/app.jar

COPY applications/${APP_NAME}/build/libs/*.jar ${APP_JAR}

ENV PORT=8080
EXPOSE ${PORT}

USER kotlin

HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:${PORT}/health || exit 1

# Replace these two lines:
# ENTRYPOINT ["/sbin/tini", "--", "java"]
# CMD [ "-Djava.security.egd=...", "-jar", "${APP_JAR}" ]

# With this single line (shell form):
CMD ["/sbin/tini", "--", "sh", "-c", "exec java \
  -Djava.security.egd=file:/dev/./urandom \
  -Dorg.slf4j.simpleLogger.dateTimeFormat=yyyy-MM-dd'T'HH:mm:ssZ \
  -Dorg.slf4j.simpleLogger.showDateTime=true \
  -Dorg.slf4j.simpleLogger.showShortLogName=true \
  -jar ${APP_JAR}"]
