

let f = document.getElementById("form");

f.addEventListener("submit", (e) => {

    let ok = true;

    let n = document.getElementById("name");
    let p = document.getElementById("pass");

    if (n.value == "") {
        let en = document.getElementById("error_name");
        en.innerHTML="Falta Nombre";
        ok = false;
    }
    if (p.value == "") {
        let ep = document.getElementById("error_pass");
        ep.innerHTML="Falta Password";
        ok = false;
    }

    if (!ok) e.preventDefault();

  });