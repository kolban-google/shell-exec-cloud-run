PROJECT_ID=$(shell gcloud config get-value core/project)
all:
	@echo "build  - Build the docker image"
	@echo "deploy - Deploy the image to Cloud Run"
	@echo "clean  - Clean resoruces created in this test"
	@echo "call   - Call the Cloud Run service"

deploy:
	gcloud run deploy cloud-run-exec-python \
		--image gcr.io/$(PROJECT_ID)/cloud-run-exec-python \
		--max-instances 1 \
		--platform managed \
		--region us-central1 \
		--no-allow-unauthenticated

build:
	gcloud builds submit --tag gcr.io/$(PROJECT_ID)/cloud-run-exec-python

clean:
	-gcloud container images delete gcr.io/$(PROJECT_ID)/cloud-run-exec-python --quiet
	-gcloud run services delete cloud-run-exec-python \
		--platform managed \
		--region us-central1 \
		--quiet

call:
	@echo "Calling Python Cloud Run service"
	@url=$(shell gcloud run services describe cloud-run-exec-python --format='value(status.url)' --region us-central1 --platform managed); \
	token=$(shell gcloud auth print-identity-token); \
	curl --request POST \
  		--header "Authorization: Bearer $$token" \
  		--header "Content-Type: text/plain" \
  		$$url/exec \
  		--data-binary "ps -ef"