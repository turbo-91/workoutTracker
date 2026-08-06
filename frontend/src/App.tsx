import { useState } from 'react'
import WorkoutPlanCard from './components/WorkoutPlanCard.tsx'
import './App.css'
import styled from "styled-components";

const Dropdown = styled.select`
  width: 20vw;
  padding: 4px;
  border: none;
  border-radius: 3px;
  background-color: grey;
  font-size: 1rem;
  cursor: pointer;

  &:focus {
    outline: none;
    border-color: #0070f3;
  }
`;

function App() {
  const [id, setId] = useState(0)


    const options = [
        { label: "select plan", value: "" },
        { label: "DAY 1", value: 3 },
        { label: "DAY 2", value: 1 },
        { label: "DAY 3", value: 4 },
        { label: "DAY 4", value: 2 },
    ];

    function handleSelect(event) {
        setId(event.target.value);
    }

  return (
    <>
      <section id="center">
          <Dropdown id="country-select"
                    value={id}
                    onChange={handleSelect}>
              {options.map((option) => (
                  <option value={option.value} key={option.value}>
                      {option.label}
                  </option>
              ))}
          </Dropdown>
        <WorkoutPlanCard id={id} />
      </section>


    </>
  )
}

export default App
