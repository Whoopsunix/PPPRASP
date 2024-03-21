mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

# rm -r ../sandbox/sandbox-module/*
if [ "$(ls -A ../sandbox/sandbox-module/)" ]; then
    rm -r ../sandbox/sandbox-module/*
fi

mv target/ppprasp-agent-1.0.0-jar-with-dependencies.jar ../sandbox/sandbox-module/