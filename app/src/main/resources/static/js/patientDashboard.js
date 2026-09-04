import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import {
  getDoctors,
  filterDoctors
} from "./services/doctorServices.js";
import {
  patientLogin,
  patientSignup
} from "./services/patientServices.js";


document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();

  const signupButton = document.getElementById("patientSignup");
  if (signupButton) {
    signupButton.addEventListener("click", () => {
      openModal("patientSignup");
    });
  }

  const loginButton = document.getElementById("patientLogin");
  if (loginButton) {
    loginButton.addEventListener("click", () => {
      openModal("patientLogin");
    });
  }

  const searchBar = document.getElementById("searchBar");
  const filterTime = document.getElementById("filterTime");
  const filterSpecialty = document.getElementById("filterSpecialty");

  if (searchBar) {
    searchBar.addEventListener("input", filterDoctorsOnChange);
  }

  if (filterTime) {
    filterTime.addEventListener("change", filterDoctorsOnChange);
  }

  if (filterSpecialty) {
    filterSpecialty.addEventListener("change", filterDoctorsOnChange);
  }
});


async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error loading doctors:", error);

    const contentDiv = document.getElementById("content");

    if (contentDiv) {
      contentDiv.innerHTML =
          "<p>Failed to load doctors.</p>";
    }
  }
}


async function filterDoctorsOnChange() {
  const name =
      document.getElementById("searchBar")?.value.trim() || null;

  const time =
      document.getElementById("filterTime")?.value || null;

  const specialty =
      document.getElementById("filterSpecialty")?.value || null;

  try {
    const result = await filterDoctors(
        name,
        time,
        specialty
    );

    const doctors = result.doctors || [];

    const contentDiv = document.getElementById("content");

    if (!contentDiv) {
      return;
    }

    contentDiv.innerHTML = "";

    if (doctors.length === 0) {
      contentDiv.innerHTML =
          "<p>No doctors found with the given filters.</p>";
      return;
    }

    doctors.forEach(doctor => {
      contentDiv.appendChild(createDoctorCard(doctor));
    });

  } catch (error) {
    console.error("Error filtering doctors:", error);

    const contentDiv = document.getElementById("content");

    if (contentDiv) {
      contentDiv.innerHTML =
          "<p>Failed to filter doctors.</p>";
    }
  }
}


function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");

  if (!contentDiv) {
    return;
  }

  contentDiv.innerHTML = "";

  if (!doctors || doctors.length === 0) {
    contentDiv.innerHTML = "<p>No doctors found.</p>";
    return;
  }

  doctors.forEach(doctor => {
    const doctorCard = createDoctorCard(doctor);
    contentDiv.appendChild(doctorCard);
  });
}


window.signupPatient = async function () {
  const name = document.getElementById("patientName")?.value.trim();
  const email = document.getElementById("patientEmail")?.value.trim();
  const password = document.getElementById("patientPassword")?.value;
  const phone = document.getElementById("patientPhone")?.value.trim();
  const address = document.getElementById("patientAddress")?.value.trim();

  const patient = {
    name,
    email,
    password,
    phone,
    address
  };

  try {
    const result = await patientSignup(patient);

    if (result.success) {
      alert(result.message || "Signup successful!");

      const modal = document.getElementById("modal");

      if (modal) {
        modal.style.display = "none";
      }

      window.location.reload();
    } else {
      alert(result.message || "Signup failed.");
    }

  } catch (error) {
    console.error("Patient signup error:", error);
    alert("An unexpected error occurred during signup.");
  }
};


window.loginPatient = async function () {
  const email = document.getElementById("patientLoginEmail")?.value.trim();
  const password = document.getElementById("patientLoginPassword")?.value;

  const patient = {
    email,
    password
  };

  try {
    const response = await patientLogin(patient);

    if (response && response.ok) {
      const data = await response.json();

      localStorage.setItem("token", data.token);
      localStorage.setItem("userRole", "loggedPatient");

      window.location.href =
          "/pages/loggedPatientDashboard.html";

    } else {
      alert("Invalid email or password.");
    }

  } catch (error) {
    console.error("Patient login error:", error);
    alert("An unexpected error occurred during login.");
  }
};