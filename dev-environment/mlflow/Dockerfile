FROM continuumio/miniconda3:4.9.2
RUN apt-get update -qq

ENV MLFLOW_HOME=/opt/mlflow \
    MLFLOW_WORK_DIR=/home/mlflow \
    MLFLOW_SERVER_HOST=0.0.0.0 \
    MLFLOW_SERVER_PORT=5000 \
    MODEL_1_PORT=1111 \
    MODEL_2_PORT=2222 \
    MODEL_3_PORT=3333

EXPOSE ${MLFLOW_SERVER_PORT}/tcp

WORKDIR ${MLFLOW_WORK_DIR}
COPY deps/ deps/
RUN conda install --yes -q -c anaconda --file=deps/anaconda-deps.txt && \
    conda install --yes -q -c conda-forge --file=deps/conda-forge-deps.txt
COPY models/ models/

WORKDIR ${MLFLOW_HOME}
COPY scripts/ scripts/
RUN chmod +x scripts/run.sh
CMD scripts/run.sh
