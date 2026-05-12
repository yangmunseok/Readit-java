function loadLibraries(isbn) {
  let url = ""
  let regions = document.querySelector("select").value.split(",")
  url = `/libraries?isbn=${isbn}&region=${regions[0]}&dtlRegion=${regions[1]}`
  fetch(url)
  .then(res => res.text())
  .then(html => {
    document.querySelector('#available-libraries').outerHTML = html;
    const availableLibraries = document.getElementById('available-libraries');
    if (availableLibraries.children.length === 0) {
      const img = document.createElement('img');
      img.src = "/images/NoResult.png";
      img.classList.add("size-100");
      availableLibraries.appendChild(img);
    }
  });

}