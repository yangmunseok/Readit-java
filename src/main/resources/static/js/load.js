function loadLibraries(isbn) {
  let url = ""
  let regions = document.querySelector("select").value.split(",")
  if (regions.length === 2) {
    url = `/libraries?isbn=${isbn}&region=${regions[0]}`
  } else {
    url = `/libraries?isbn=${isbn}&region=${regions[0]}&dtl_region=${regions[1]}`
  }
  fetch(url)
  .then(res => res.text())
  .then(html => {
    document.querySelector('#available-libraries').outerHTML = html;
  });

}