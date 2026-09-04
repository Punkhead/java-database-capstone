import { openModal } from "./components/modals.js";
import {
    getDoctors,
    filterDoctors,
    saveDoctor
} from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";


document.addEventListener("DOMContentLoaded", () => {

    const addDoctorButton = document.getElementById("addDocBtn");

    if (addDoctorButton) {
        addDoctorButton.addEventListener("click", () => {
            openModal("addDoctor");
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

    loadDoctorCards();
});


async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Error loading doctors:", error);
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
        const result = await filterDoctors(name, time, specialty);
        const doctors = result.doctors || [];

        if (doctors.length === 0) {
            const contentDiv = document.getElementById("content");
            contentDiv.innerHTML = "<p>No doctors found</p>";
            return;
        }

        renderDoctorCards(doctors);

    } catch (error) {
        console.error("Error filtering doctors:", error);
        alert("An error occurred while filtering doctors.");
    }
}


function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");

    if (!contentDiv) {
        return;
    }

    contentDiv.innerHTML = "";

    doctors.forEach(doctor => {
        const doctorCard = createDoctorCard(doctor);
        contentDiv.appendChild(doctorCard);
    });
}


async function adminAddDoctor(event) {
    event.preventDefault();

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Authentication token not found.");
        return;
    }

    const availableTimes = Array.from(
        document.querySelectorAll(
            'input[name="availableTimes"]:checked'
        )
    ).map(input => input.value);

    const doctor = {
        name: document.getElementById("doctorName").value.trim(),
        specialty: document.getElementById("doctorSpecialty").value.trim(),
        email: document.getElementById("doctorEmail").value.trim(),
        password: document.getElementById("doctorPassword").value,
        phone: document.getElementById("doctorPhone").value.trim(),
        availableTimes: availableTimes
    };

    try {
        const result = await saveDoctor(doctor, token);

        alert(result.message);

        if (result.success) {
            const modal = document.getElementById("modal");

            if (modal) {
                modal.style.display = "none";
            }

            await loadDoctorCards();
        }

    } catch (error) {
        console.error("Error adding doctor:", error);
        alert("Failed to add doctor.");
    }
}