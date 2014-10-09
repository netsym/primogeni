#get all of the env vars and user options
ifneq ($(INCLUDED_INC), true)
include /Users/liux/Workspace/primogeni/netsim/Makefile.inc 
endif

# You can set these variables from the command line.
SPHINXOPTS    =
SPHINXBUILD   = sphinx-build
PAPER         =
BUILDDIR      = $(DOC_DIR)/_build
DOXYGEN       = doxygen

# Internal variables.
PAPEROPT_a4     = -D latex_paper_size=a4
PAPEROPT_letter = -D latex_paper_size=letter
ALLSPHINXOPTS   = -d $(BUILDDIR)/doctrees $(PAPEROPT_$(PAPER)) $(SPHINXOPTS) $(DOC_DIR)

DOXYDEPS = .jprime_done .slingshot_done .controller_done .ssf_done .ssfnet_done
CLEANUP = CONTROLLER.tag JPRIME.tag SLINGSHOT.tag SSF.tag SSFNET.tag /Users/liux/Workspace/primogeni/netsim/../doc/doxy_predef.cfg $(DOXYDEPS)
.PHONY: doc-verbose-help doc-help doc-clean html dirhtml singlehtml pickle json htmlhelp qthelp devhelp epub latex latexpdf text man changes linkcheck doc-test web

doc-verbose-help:
	@echo "Please use \`make <target>' where <target> is one of"
	@echo "  html       to make standalone HTML files"
	@echo "  dirhtml    to make HTML files named index.html in directories"
	@echo "  singlehtml to make a single large HTML file"
	@echo "  pickle     to make pickle files"
	@echo "  json       to make JSON files"
	@echo "  htmlhelp   to make HTML files and a HTML help project"
	@echo "  qthelp     to make HTML files and a qthelp project"
	@echo "  devhelp    to make HTML files and a Devhelp project"
	@echo "  epub       to make an epub"
	@echo "  latex      to make LaTeX files, you can set PAPER=a4 or PAPER=letter"
	@echo "  latexpdf   to make LaTeX files and run them through pdflatex"
	@echo "  text       to make text files"
	@echo "  man        to make manual pages"
	@echo "  changes    to make an overview of all changed/added/deprecated items"
	@echo "  linkcheck  to check all external links for integrity"
	@echo "  doc-test   to run all doctests embedded in the documentation (if enabled)"
	@echo "  doc-clean  to clean up the documentation"
	@echo "  doxy-xml   generate the doxygen xml files for the Java/C++/Jython APIs"


doc-help:
	@ $(ECHO) "    doc-verbose-help  :  Get infomation on the what documentation can be build."

$(BUILDDIR)/doxygen:
	mkdir $(BUILDDIR)/doxygen
	$(PERL) /Users/liux/Workspace/primogeni/netsim/../doc/predef.pl $(SSFNET_CFLAGS) > /Users/liux/Workspace/primogeni/netsim/../doc/doxy_predef.cfg
.jprime_done: $(BUILDDIR)/doxygen /Users/liux/Workspace/primogeni/netsim/../doc/jprime.doxyfile
	mkdir -p $(BUILDDIR)/doxygen/jprime
	$(DOXYGEN) /Users/liux/Workspace/primogeni/netsim/../doc/jprime.doxyfile
	touch .jprime_done
.slingshot_done: $(BUILDDIR)/doxygen /Users/liux/Workspace/primogeni/netsim/../doc/slingshot.doxyfile
	mkdir -p $(BUILDDIR)/doxygen/slingshot
	$(DOXYGEN) /Users/liux/Workspace/primogeni/netsim/../doc/slingshot.doxyfile
	touch .slingshot_done
.controller_done: $(BUILDDIR)/doxygen /Users/liux/Workspace/primogeni/netsim/../doc/controller.doxyfile
	mkdir -p $(BUILDDIR)/doxygen/meta-controller
	$(DOXYGEN) /Users/liux/Workspace/primogeni/netsim/../doc/controller.doxyfile
	touch .controller_done
.ssf_done: $(BUILDDIR)/doxygen /Users/liux/Workspace/primogeni/netsim/../doc/ssf.doxyfile
	mkdir -p $(BUILDDIR)/doxygen/ssf
	$(DOXYGEN) /Users/liux/Workspace/primogeni/netsim/../doc/ssf.doxyfile
	touch .ssf_done
.ssfnet_done: $(BUILDDIR)/doxygen /Users/liux/Workspace/primogeni/netsim/../doc/ssfnet.doxyfile
	mkdir -p $(BUILDDIR)/doxygen/ssfnet
	$(DOXYGEN) /Users/liux/Workspace/primogeni/netsim/../doc/ssfnet.doxyfile
	touch .ssfnet_done

doc-clean:
	-rm -rf $(BUILDDIR)/* $(CLEANUP) web.tgz

html: $(DOXYDEPS)
	$(SPHINXBUILD) -b html $(ALLSPHINXOPTS) $(BUILDDIR)/html
	@echo
	@echo "Build finished. The HTML pages are in $(BUILDDIR)/html."

dirhtml: $(DOXYDEPS)
	$(SPHINXBUILD) -b dirhtml $(ALLSPHINXOPTS) $(BUILDDIR)/dirhtml
	@echo
	@echo "Build finished. The HTML pages are in $(BUILDDIR)/dirhtml."

singlehtml: $(DOXYDEPS)
	$(SPHINXBUILD) -b singlehtml $(ALLSPHINXOPTS) $(BUILDDIR)/singlehtml
	@echo
	@echo "Build finished. The HTML page is in $(BUILDDIR)/singlehtml."

pickle: $(DOXYDEPS)
	$(SPHINXBUILD) -b pickle $(ALLSPHINXOPTS) $(BUILDDIR)/pickle
	@echo
	@echo "Build finished; now you can process the pickle files."

json: $(DOXYDEPS)
	$(SPHINXBUILD) -b json $(ALLSPHINXOPTS) $(BUILDDIR)/json
	@echo
	@echo "Build finished; now you can process the JSON files."

htmlhelp: $(DOXYDEPS)
	$(SPHINXBUILD) -b htmlhelp $(ALLSPHINXOPTS) $(BUILDDIR)/htmlhelp
	@echo
	@echo "Build finished; now you can run HTML Help Workshop with the" \
	      ".hhp project file in $(BUILDDIR)/htmlhelp."

qthelp: $(DOXYDEPS)
	$(SPHINXBUILD) -b qthelp $(ALLSPHINXOPTS) $(BUILDDIR)/qthelp
	@echo
	@echo "Build finished; now you can run "qcollectiongenerator" with the" \
	      ".qhcp project file in $(BUILDDIR)/qthelp, like this:"
	@echo "# qcollectiongenerator $(BUILDDIR)/qthelp/PRIMEX.qhcp"
	@echo "To view the help file:"
	@echo "# assistant -collectionFile $(BUILDDIR)/qthelp/PRIMEX.qhc"

devhelp: $(DOXYDEPS)
	$(SPHINXBUILD) -b devhelp $(ALLSPHINXOPTS) $(BUILDDIR)/devhelp
	@echo
	@echo "Build finished."
	@echo "To view the help file:"
	@echo "# mkdir -p $$HOME/.local/share/devhelp/PRIMEX"
	@echo "# ln -s $(BUILDDIR)/devhelp $$HOME/.local/share/devhelp/PRIMEX"
	@echo "# devhelp"

epub: $(DOXYDEPS)
	$(SPHINXBUILD) -b epub $(ALLSPHINXOPTS) $(BUILDDIR)/epub
	@echo
	@echo "Build finished. The epub file is in $(BUILDDIR)/epub."

latex: $(DOXYDEPS)
	$(SPHINXBUILD) -b latex $(ALLSPHINXOPTS) $(BUILDDIR)/latex
	@echo
	@echo "Build finished; the LaTeX files are in $(BUILDDIR)/latex."
	@echo "Run \`make' in that directory to run these through (pdf)latex" \
	      "(use \`make latexpdf' here to do that automatically)."

latexpdf: $(DOXYDEPS)
	$(SPHINXBUILD) -b latex $(ALLSPHINXOPTS) $(BUILDDIR)/latex
	@echo "Running LaTeX files through pdflatex..."
	make -C $(BUILDDIR)/latex all-pdf
	@echo "pdflatex finished; the PDF files are in $(BUILDDIR)/latex."

text: $(DOXYDEPS)
	$(SPHINXBUILD) -b text $(ALLSPHINXOPTS) $(BUILDDIR)/text
	@echo
	@echo "Build finished. The text files are in $(BUILDDIR)/text."

man: $(DOXYDEPS)
	$(SPHINXBUILD) -b man $(ALLSPHINXOPTS) $(BUILDDIR)/man
	@echo
	@echo "Build finished. The manual pages are in $(BUILDDIR)/man."

changes: $(DOXYDEPS)
	$(SPHINXBUILD) -b changes $(ALLSPHINXOPTS) $(BUILDDIR)/changes
	@echo
	@echo "The overview file is in $(BUILDDIR)/changes."

linkcheck: $(DOXYDEPS)
	$(SPHINXBUILD) -b linkcheck $(ALLSPHINXOPTS) $(BUILDDIR)/linkcheck
	@echo
	@echo "Link check complete; look for any errors in the above output " \
	      "or in $(BUILDDIR)/linkcheck/output.txt."

doc-test: $(DOXYDEPS)
	$(SPHINXBUILD) -b doctest $(ALLSPHINXOPTS) $(BUILDDIR)/doctest
	@echo "Testing of doctests in the sources finished, look at the " \
	      "results in $(BUILDDIR)/doctest/output.txt."

web: html
	mv $(BUILDDIR)/html $(BUILDDIR)/man
	tar -zcf web.tgz ./_build/man ./_build/doxygen
	mv $(BUILDDIR)/man $(BUILDDIR)/html
