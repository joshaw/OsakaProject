PROJECT=mainfile
TEX=pdflatex
BIBTEX=bibtex -terse
DIR=build
BUILDTEX=$(TEX) -output-directory=$(DIR) -halt-on-error $(PROJECT).tex

CHECKDIR=mkdir -p $(DIR)

all:
	$(CHECKDIR)
	$(BUILDTEX)
	$(BIBTEX) ${DIR}/$(PROJECT) || true
	$(BUILDTEX)
	echo -e "\n\n\n"
	$(BUILDTEX) | grep -i "warning" -A2
clean-all:
	find ${DIR}/ -type f -delete

clean:
	find ${DIR}/ -type f ! -name "*.pdf" -delete

# vim:ft=make
