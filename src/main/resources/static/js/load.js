function loadLibraries(isbn) {
  let url = ""
  let regions = document.querySelector("select").value.split(",")
  url = `/libraries?isbn=${isbn}&region=${regions[0]}&dtl_region=${regions[1]}`
  fetch(url)
  .then(res => res.text())
  .then(html => {
    document.querySelector('#available-libraries').outerHTML = html;
  });

}